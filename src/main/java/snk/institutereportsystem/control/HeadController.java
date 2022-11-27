package snk.institutereportsystem.control;

import snk.institutereportsystem.entity.*;

import java.util.List;

public class HeadController {
    private JDBCRepository repository = JDBCRepository.getInstance();


    public Iterable<User> getEmployers() {
        return repository.getUsersByRole(Role.EMPLOYEE);
    }

    public Iterable<Theme> getThemes(User user) {
        return repository.getThemesByUser(user);
    }

    public List<Theme> getFreeThemes() {
        return repository.getFreeThemes();
    }

    public void assignTheme(User selectedUser, Theme selectedTheme) {
        repository.assignTheme(selectedUser.getId(),selectedTheme.getId());
    }

    public List<Report> getUncheckedReports() {
        return repository.getUncheckedReports();
    }

    public User getUser(long userId) {
        return repository.getUser(userId);
    }

    public String getThemeName(long themeId) {
        return repository.getTheme(themeId).getName();
    }

    public void approveReport(Report selectedReport) {
        repository.changeReportStatus(Status.APPROVED, selectedReport.getReportId());
    }

    public void declineReport(Report selectedReport) {
        repository.changeReportStatus(Status.DECLINED, selectedReport.getReportId());
    }
}
