package com.escrowlite.escrowlite.service;

import com.escrowlite.escrowlite.dto.CreateMilestoneRequest;
import com.escrowlite.escrowlite.dto.CreateProjectRequest;
import com.escrowlite.escrowlite.dto.MilestoneSummaryResponse;
import com.escrowlite.escrowlite.dto.ProjectSummaryResponse;
import com.escrowlite.escrowlite.dto.ReworkRequest;
import com.escrowlite.escrowlite.exception.InvalidBusinessRuleException;
import com.escrowlite.escrowlite.exception.ResourceNotFoundException;
import com.escrowlite.escrowlite.model.Client;
import com.escrowlite.escrowlite.model.Freelancer;
import com.escrowlite.escrowlite.model.Milestone;
import com.escrowlite.escrowlite.model.MilestoneStatus;
import com.escrowlite.escrowlite.model.Project;
import com.escrowlite.escrowlite.model.Release;
import com.escrowlite.escrowlite.repository.ClientRepository;
import com.escrowlite.escrowlite.repository.FreelancerRepository;
import com.escrowlite.escrowlite.repository.MilestoneRepository;
import com.escrowlite.escrowlite.repository.ProjectRepository;
import com.escrowlite.escrowlite.repository.ReleaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EscrowService {

    private final ClientRepository clientRepository;
    private final FreelancerRepository freelancerRepository;
    private final ProjectRepository projectRepository;
    private final MilestoneRepository milestoneRepository;
    private final ReleaseRepository releaseRepository;

    public EscrowService(
            ClientRepository clientRepository,
            FreelancerRepository freelancerRepository,
            ProjectRepository projectRepository,
            MilestoneRepository milestoneRepository,
            ReleaseRepository releaseRepository
    ) {
        this.clientRepository = clientRepository;
        this.freelancerRepository = freelancerRepository;
        this.projectRepository = projectRepository;
        this.milestoneRepository = milestoneRepository;
        this.releaseRepository = releaseRepository;
    }

    public Project createProject(CreateProjectRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client not found with id: " + request.getClientId()));
        Freelancer freelancer = freelancerRepository.findById(request.getFreelancerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Freelancer not found with id: " + request.getFreelancerId()));

        BigDecimal milestoneTotal = request.getMilestones().stream()
                .map(CreateMilestoneRequest::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (milestoneTotal.compareTo(request.getTotalAmount()) != 0) {
            throw new InvalidBusinessRuleException(
                    "Sum of milestone amounts must equal the project total agreed amount");
        }

        Project project = Project.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .totalAmount(request.getTotalAmount())
                .client(client)
                .freelancer(freelancer)
                .build();

        List<Milestone> milestones = request.getMilestones().stream()
                .map(milestoneRequest -> Milestone.builder()
                        .title(milestoneRequest.getTitle())
                        .amount(milestoneRequest.getAmount())
                        .status(MilestoneStatus.PENDING)
                        .project(project)
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));
        project.setMilestones(milestones);

        return projectRepository.save(project);
    }

    public Milestone deliverMilestone(Long milestoneId) {
        Milestone milestone = findMilestone(milestoneId);
        milestone.setStatus(MilestoneStatus.DELIVERED);
        return milestoneRepository.save(milestone);
    }

    public Milestone approveMilestone(Long milestoneId) {
        Milestone milestone = findMilestone(milestoneId);
        if (milestone.getStatus() != MilestoneStatus.DELIVERED) {
            throw new InvalidBusinessRuleException("Only DELIVERED milestones can be APPROVED");
        }
        milestone.setStatus(MilestoneStatus.APPROVED);
        return milestoneRepository.save(milestone);
    }

    public Milestone requestRework(Long milestoneId, ReworkRequest reworkRequest) {
        Milestone milestone = findMilestone(milestoneId);
        if (milestone.getStatus() != MilestoneStatus.DELIVERED) {
            throw new InvalidBusinessRuleException(
                    "Only DELIVERED milestones can be requested for rework");
        }
        milestone.setStatus(MilestoneStatus.REWORK_REQUESTED);
        milestone.setReworkNotes(reworkRequest.getNotes());
        return milestoneRepository.save(milestone);
    }

    public Release releaseMilestonePayment(Long milestoneId) {
        Milestone milestone = findMilestone(milestoneId);
        if (milestone.getStatus() != MilestoneStatus.APPROVED) {
            throw new InvalidBusinessRuleException(
                    "Payment can only be released after explicit client approval");
        }
        if (releaseRepository.existsByMilestone(milestone)) {
            throw new InvalidBusinessRuleException(
                    "Payment for this milestone has already been released");
        }

        Release release = Release.builder()
                .project(milestone.getProject())
                .milestone(milestone)
                .amountReleased(milestone.getAmount())
                .releasedAt(LocalDateTime.now())
                .build();
        Release savedRelease = releaseRepository.save(release);

        milestone.setStatus(MilestoneStatus.RELEASED);
        milestoneRepository.save(milestone);
        return savedRelease;
    }

    @Transactional(readOnly = true)
    public ProjectSummaryResponse getProjectSummary(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
        BigDecimal totalReleasedAmount = releaseRepository.findTotalReleasedAmountByProjectId(projectId);
        if (totalReleasedAmount == null) {
            totalReleasedAmount = BigDecimal.ZERO;
        }

        List<MilestoneSummaryResponse> milestones = project.getMilestones().stream()
                .map(milestone -> new MilestoneSummaryResponse(
                        milestone.getId(),
                        milestone.getTitle(),
                        milestone.getAmount(),
                        milestone.getStatus()))
                .toList();

        return new ProjectSummaryResponse(
                project.getId(),
                project.getTitle(),
                project.getClient().getName(),
                project.getFreelancer().getName(),
                project.getTotalAmount(),
                totalReleasedAmount,
                project.getTotalAmount().subtract(totalReleasedAmount),
                milestones);
    }

    private Milestone findMilestone(Long milestoneId) {
        return milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Milestone not found with id: " + milestoneId));
    }
}