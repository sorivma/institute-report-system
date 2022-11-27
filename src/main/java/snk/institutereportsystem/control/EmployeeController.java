package snk.institutereportsystem.control;

import snk.institutereportsystem.entity.JDBCRepository;
import snk.institutereportsystem.entity.Report;
import snk.institutereportsystem.entity.Status;
import snk.institutereportsystem.entity.User;

import java.util.List;

public class EmployeeController {
    JDBCRepository repository = JDBCRepository.getInstance();


    public List<Report> getReports(User currentUser) {
        return repository.getReports(currentUser.getId());
    }

    public String getReportName(long themeId) {
        return repository.getTheme(themeId).getName();
    }

    public void saveReport(Report report) {
        repository.setReportText(report.getReportId(), report.getText());
    }

    public void sendReport(Report selectedReport) {
        repository.changeReportStatus(Status.UNCHECKED, selectedReport.getReportId());
    }
}
