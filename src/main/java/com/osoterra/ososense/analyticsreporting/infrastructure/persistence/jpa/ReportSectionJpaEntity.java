package com.osoterra.ososense.analyticsreporting.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "report_sections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportSectionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plot_report_id", nullable = false)
    private Long plotReportId;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(name = "section_type", nullable = false, length = 40)
    private String sectionType;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;
}
