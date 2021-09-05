package orange_hrm.tests;

import io.qameta.allure.Description;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static orange_hrm.page_objects.AddCandidatePage.*;
import static orange_hrm.page_objects.AddUserPage.*;
import static orange_hrm.page_objects.AdminPage.*;
import static orange_hrm.page_objects.AssignLeavePage.*;
import static orange_hrm.page_objects.DashboardPage.*;
import static orange_hrm.page_objects.JobTitlesPage.*;
import static orange_hrm.page_objects.LeaveListPage.*;
import static orange_hrm.page_objects.LoginPage.*;
import static orange_hrm.page_objects.MyInfoPage.*;
import static orange_hrm.page_objects.OrganizationStructurePage.*;
import static orange_hrm.page_objects.PIMPage.*;
import static orange_hrm.page_objects.PersonalDetailsPage.*;
import static orange_hrm.page_objects.RecruitmentPage.*;
import static utils.Driver.getMaximizedWindow;
import static utils.helpers.AddCandidateHelper.*;
import static utils.helpers.AddUserHelper.*;
import static utils.helpers.AssignLeaveHelper.*;
import static utils.helpers.JobTitleHelper.*;
import static utils.helpers.PhotoHelper.*;
import static utils.helpers.SalesEmployeeHelper.*;
import static utils.helpers.StructureHelper.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrangeTests {

    @BeforeAll
    public void setUp() {
        getMaximizedWindow();
    }

    @BeforeEach
    @Description("Test for login to https://opensource-demo.orangehrmlive.com/")
    @Order(1)
    public void loginTest() throws IOException {

        openLoginPage();
        login();
        getDashboardPageTitle().shouldBe(visible);
    }

    @Description("Test for adding new user")
    @Ignore
    @Order(2)
    public void addUserTest() throws IOException {

        clickAdminTab();
        clickAddUserButton();

        getUserRoleField().shouldBe(visible);
        getEmployeeNameField().shouldBe(visible);
        getUsernameField().shouldBe(visible);
        getStatusField().shouldBe(visible);
        getPasswordField().shouldBe(visible);
        getConfirmPasswordField().shouldBe(visible);

        selectUserRole(getESSUserRole());
        enterEmployeeNameForAddUser(getEmployeeName());
        enterUsername(getNewUsername());
        selectStatus(getEnabledStatus());
        enterPassword(getNewUserPassword());
        enterConfirmPassword(getNewUserConfirmPassword());

        clickSaveUserButton();

        findNewUser().shouldBe(exist);

    }

    @Description("Test for adding and deleting three job titles")
    @Order(3)
    @Test
    public void addAndDeleteThreeJobTitlesTest() throws IOException {

        clickJobTitlesLink();

        getJobTitlesList().forEach(
                elem -> {
                    clickAddJobTitleButton();
                    fillJobTitleField(elem);
                    clickSaveNewJobTitleButton();
                    getNewJobTitle(elem).shouldBe(visible);
                    int sizeJobTitlesCollectionBeforeDeletion = getJobTitlesCollection().size();

                    getNewJobTitle(elem).$(getJobTitleCheckbox()).setSelected(true);

                    clickDeleteNewJobTitleButton();
                    clickDeleteConfirmationButton();

                    getJobTitlesCollection().shouldHave(size(sizeJobTitlesCollectionBeforeDeletion - 1));
                }
        );
    }

    @Description("Test for adding new candidate")
    @Order(4)
    @Test
    public void addCandidateTest() throws IOException {

        clickCandidatesLink();
        clickAddCandidatesButton();

        enterFullName(getCandidateFirstName(), getCandidateLastName());
        enterEmail(getCandidateEmail());
        enterContactNo(getCandidateContactNo());
        selectJobVacancy(getCandidateJobVacancy());
        addResume(getCandidateResume());
        setApplicationDate(getCandidateApplicationDate());
        selectCheckbox();

        clickSaveCandidateButton();

        getNewCandidate(getCandidateLastName()).shouldBe(exist);

    }

    @Description("Test for assigning leave by employee")
    @Order(5)
    @Test
    public void assignLeaveTest() throws IOException {

        clickAssignLeaveLink();

        getAssignLeaveEmployeeNameField().shouldBe(visible);
        getAssignLeaveTypeField().shouldBe(visible);
        getAssignLeaveFromDateField().shouldBe(visible);
        getAssignLeaveToDateField().shouldBe(visible);

        enterEmployeeNameForAssignLeave(getAssignLeaveEmployeeName());
        selectLeaveType(getAssignLeaveType());
        selectFromDate(getAssignLeaveFromDate());
        selectToDate(getAssignLeaveToDate());

        clickAssignButton();

        findAssignLeave(getAssignLeaveDates()).shouldBe(visible);
    }

    @Description("Test for checking dashboard to watch all elements")
    @Order(6)
    @Test
    public void checkDashboardTest() {

        getAssignLeaveButton().shouldBe(visible);
        getLeaveListButton().shouldBe(visible);
        getTimesheetsButton().shouldBe(visible);
        getApplyLeaveButton().shouldBe(visible);
        getMyLeaveButton().shouldBe(visible);
        getMyTimesheetButton().shouldBe(visible);
        getEmployeeDistributionBySubunitDiagram().shouldBe(visible);
        getLegendComponent().shouldBe(visible);
        getPendingLeaveRequestsComponent().shouldBe(visible);
    }

    @Description("Test for checking any employee from sales subunit")
    @Order(7)
    @Test
    public void checkSalesEmployeeTest() throws IOException {

        clickPIMPageLink();
        findSalesEmployee(getSalesSubUnit());

        getFirstNameField().shouldHave(exactValue(getSalesFirstName()));
        getLastNameField().shouldHave(exactValue(getSalesLastName()));
        getEmployeeIdField().shouldHave(exactValue(getSalesId()));
        getMaleGenderRadiobutton().shouldBe(checked);
        getMaritalStatusField().shouldHave(exactValue(getSalesMaritalStatus()));
        getNationalityField().shouldHave(text(getSalesNationality()));
        getDateOfBirthField().shouldHave(exactValue(getSalesDateOfBirth()));
    }

    @Description("Test for editing organisation structure by adding and deleting new department")
    @Order(8)
    @Test
    public void editOrganizationStructureTest() throws IOException {

        clickOrganizationStructureLink();

        clickEditOrganizationStructureButton();
        addNewDepartment();
        enterDepartmentName(getNewDepartment());
        clickSaveDepartmentButton();
        clickDoneButton();

        int collectionSizeBeforeDeleting = getSalesAndMarketingCollection().size();

        clickEditOrganizationStructureButton();
        deleteNewDepartment();
        confirmDeleteNewDepartment();
        clickDoneButton();

        getSalesAndMarketingCollection().shouldHave(size(collectionSizeBeforeDeleting - 1));

    }

    @Description("Test for changing employee photo by uploading new one")
    @Order(9)
    @Test
    public void changeEmployeePhotoTest() throws IOException {

        clickMyInfoTab();

        clickEmployeePhotoLink();
        selectPhotoFile(getEmployeePhotoPath());
        clickUploadButton();

        getSuccessMessage().shouldBe(visible);

    }

    @Description("Test for logout from system")
    @Order(10)
    @Test
    public void logoutTest() {

        clickWelcomeDropList();
        clickLogout();

        getLoginPanel().shouldBe(visible);
    }

}
