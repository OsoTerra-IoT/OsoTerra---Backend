CREATE TABLE report_sections (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    plot_report_id  BIGINT       NOT NULL REFERENCES plot_reports (id),
    title           VARCHAR(120) NOT NULL,
    section_type    VARCHAR(40)  NOT NULL,
    content         TEXT         NOT NULL,
    display_order   INT          NOT NULL
);
