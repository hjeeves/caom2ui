/*
 ************************************************************************
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 *
 * (c) 2014.                         (c) 2014.
 * National Research Council            Conseil national de recherches
 * Ottawa, Canada, K1A 0R6              Ottawa, Canada, K1A 0R6
 * All rights reserved                  Tous droits reserves
 *
 * NRC disclaims any warranties         Le CNRC denie toute garantie
 * expressed, implied, or statu-        enoncee, implicite ou legale,
 * tory, of any kind with respect       de quelque nature que se soit,
 * to the software, including           concernant le logiciel, y com-
 * without limitation any war-          pris sans restriction toute
 * ranty of merchantability or          garantie de valeur marchande
 * fitness for a particular pur-        ou de pertinence pour un usage
 * pose.  NRC shall not be liable       particulier.  Le CNRC ne
 * in any event for any damages,        pourra en aucun cas etre tenu
 * whether direct or indirect,          responsable de tout dommage,
 * special or general, consequen-       direct ou indirect, particul-
 * tial or incidental, arising          ier ou general, accessoire ou
 * from the use of the software.        fortuit, resultant de l'utili-
 *                                      sation du logiciel.
 *
 *
 * @author jenkinsd
 * 15/05/14 - 1:19 PM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */
package ca.nrc.cadc.search.integration;


import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;


public class CAOMSearchBrowserTest extends AbstractAdvancedSearchIntegrationTest
{
    private static final String FILTER_FILTER_ID =
            "caom2:Plane.energy.bandpassName_filter";
    private static final By FILTER_FILTER_BY = By.id(FILTER_FILTER_ID);
    private static final By RA_FILTER_BY =
            By.id("caom2:Plane.position.bounds.cval1_filter");
    private static final By DEC_FILTER_BY =
            By.id("caom2:Plane.position.bounds.cval2_filter");
    private static final By FIRST_QUICKSEARCH_TARGET_LINK =
            By.cssSelector("a.quicksearch_link:nth-child(1)");
    private static final By COLUMN_MANAGER_BUTTON_BY =
            By.name("slick-columnpicker-panel-change-column");
    private static final String COLUMN_PICKER_ENERGY_INTERVAL_ITEM_ID =
            "ITEM_caom2:Plane.energy.bounds";
    private static final String COLUMN_PICKER_AVAILABLE_ITEMS_CONTAINER_ID =
            "cadc_columnpicker_available_items";
    private static final By COLUMN_PICKER_SELECTED_ITEMS_CONTAINER =
            By.id("cadc_columnpicker_selected_items");
    private static final By PI_NAME_COLUMN_ADD_BY =
            By.id("column-picker-caom2:Observation.proposal.pi");
    private static final By TARGET_FORM_FIELD_BY =
            By.id("Plane.position.bounds");
    private static final By OBSERVATION_ID_FORM_FIELD_BY =
            By.id("Observation.observationID");
    private static final By ONE_CLICK_DOWNLOAD_LINK_ROW_3_ID_BY =
            By.id("_one-click_vov_3");
    private static final By INSTRUMENT_NAME_FRESH_STALE_DIVIDER =
            By.xpath("//*[@id=\"Observation.instrument.name\"]/option[@value='SPACER']");


    @Test
    public void searchCAOM() throws Exception
    {
        CAOMSearchFormPage searchFormPage =
                goTo(ENGLISH_ENDPOINT, null, CAOMSearchFormPage.class);

        searchFormPage.enterTarget("210.05  54.3");
        searchFormPage.enterObservationID("692512");

        searchFormPage.reset();

        final int index = searchFormPage.findDataTrainValueIndex(
                By.id("Observation.instrument.name"), "SPACER", false);

        verifyTrue(index > 0);

        searchFormPage.enterObservationID("f008h000");

        SearchResultsPage searchResultsPage = searchFormPage.submitSuccess();

        searchResultsPage.waitForElementPresent(
                ONE_CLICK_DOWNLOAD_LINK_ROW_3_ID_BY);

        final String currentWindow = getCurrentWindowHandle();

        final CAOMObservationDetailsPage detailsPage =
                searchResultsPage.openObservationDetails(1);

        detailsPage.waitForElementPresent(By.cssSelector("table.content"));
        detailsPage.close();

        selectWindow(currentWindow);

        searchResultsPage.quickSearchTarget();

        selectWindow(currentWindow);

        searchFormPage = searchResultsPage.queryTab();
        searchFormPage.reset();

        searchFormPage.enterTarget("M17");
        searchFormPage.enterCollection("JCMT");

        searchResultsPage = searchFormPage.submitSuccess();
        verifyEquals(searchResultsPage.getSelectedRestFrameEnergyUnit(), "GHz");

        searchFormPage = searchResultsPage.queryTab();
        searchFormPage.reset();

        searchFormPage.enterCollection("CFHTMEGAPIPE");
        searchResultsPage = searchFormPage.submitSuccess();

        verifyEquals(searchResultsPage.getSelectIQUnit(), "Arcseconds");

        searchFormPage = searchResultsPage.queryTab();
        searchFormPage.reset();

        searchFormPage.enterCollection("IRIS");
        searchResultsPage = searchFormPage.submitSuccess();
        verifyTrue(searchResultsPage.getCurrentResultsRowCount() > 0);

        searchResultsPage.filterOnRA("18:03..18:07");

        /*
        TODO - Complete for new Page Object model going forward.
        TODO - jenkinsd 2016.02.16
         */

        /*
        verifyEquals("18:03:48.78",
                     find(By.xpath(
                             "//div[@id='resultTable']/div[5]/div[3]/div/div/div[4]/span")).
                             getText());

        // Dec. column
        inputTextValue(DEC_FILTER_BY, "< 70:00");
        waitFor(3);

        verifyText(By.xpath("//div[@id='resultTable']/div[5]/div[3]/div/div/div[5]/span"),
                   "-49:59:07.6");

        waitOneSecond();
        inputTextValue(RA_FILTER_BY, "");
        inputTextValue(DEC_FILTER_BY, "");
        waitOneSecond();

        scrollGridHorizontally("resultTable");
        inputTextValue(FILTER_FILTER_BY, "10", true, false);

        waitFor(2);
        verifyText(By.xpath("//div[@id='resultTable']/div[5]/div[3]/div/div[2]/div[10]/span"),
                   "IRAS-100um");

        inputTextValue(FILTER_FILTER_BY, "");

        click(COLUMN_MANAGER_BUTTON_BY);

        waitOneSecond();

        click(PI_NAME_COLUMN_ADD_BY);
        waitOneSecond();

        if (!getInternetBrowserCommand().equals("*safari"))
        {
            hover(By.xpath("//span[@class='slick-column-name'][contains(text(), \"Collection\")]"));
        }

        waitFor(2);

        verifyElementPresent(By.className("votable_link_votable"));
        verifyElementPresent(By.className("votable_link_csv"));
        verifyElementPresent(By.className("votable_link_tsv"));

        // Next search.
        queryTab();
        resetForm();
        waitFor(2);

        // observerationID of proprietary data
        inputTextValue(OBSERVATION_ID_FORM_FIELD_BY,
                       "Observation.observationID_details", "1692420");

        searchAndWait(true, true);

        // data release of proprietary data
        queryTab();
        resetForm();
        waitFor(2);

        // Open it.
        toggleDetailsItem("Plane.dataRelease_details");
        final By publicCheckboxBy =
                By.id("Plane.dataRelease@PublicTimestampFormConstraint.value");
        checkboxOn(publicCheckboxBy);
        verifyFalse(find(
                By.id("Plane.dataRelease")).isEnabled());
        inputTextValue(OBSERVATION_ID_FORM_FIELD_BY,
                       "Observation.observationID_details", "f008h000");

        searchAndWait(true, true);

        adqlTab();
        verifyTrue(find(
                By.id("query")).getText().contains("Plane.dataRelease <="));

        // Do authenticated stuff.

        login();

        waitFor(5);
        queryTab();
        waitForElementPresent(By.id("Observation.type"));
        resetForm();
        waitFor(2);

        // observationID of proprietary data
        inputTextValue(OBSERVATION_ID_FORM_FIELD_BY,
                       "Observation.observationID_details", "1692420");

        searchAndWait(true, true);

        click(COLUMN_MANAGER_BUTTON_BY);
        waitForElementPresent(
                By.id(COLUMN_PICKER_AVAILABLE_ITEMS_CONTAINER_ID));

        waitOneSecond();

        logout();
        */
    }
}