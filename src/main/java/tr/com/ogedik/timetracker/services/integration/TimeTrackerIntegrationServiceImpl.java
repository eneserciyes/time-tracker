package tr.com.ogedik.timetracker.services.integration;

/*
 * @author enes.erciyes
 */

import org.springframework.stereotype.Service;
import tr.com.ogedik.commons.abstraction.AbstractService;
import tr.com.ogedik.commons.constants.Services;
import tr.com.ogedik.commons.rest.request.client.HttpRestClient;
import tr.com.ogedik.commons.rest.request.client.helper.RequestURLDetails;
import tr.com.ogedik.commons.rest.response.RestResponse;
import tr.com.ogedik.commons.rest.response.model.JQLSearchResult;
import tr.com.ogedik.commons.util.MapUtils;


@Service
public class TimeTrackerIntegrationServiceImpl extends AbstractService implements TimeTrackerIntegrationService {

    @Override
    public JQLSearchResult getJQLSearchResult(String username, String startDate, String endDate) {

        RequestURLDetails requestURLDetails = generateRequestInfo(Services.INTEGRATION, Services.Path.LOGGED_ISSUES,
                MapUtils.of("username", username, "startDate", startDate, "endDate", endDate));

        RestResponse<JQLSearchResult> searchResultResponse = HttpRestClient.doGet(requestURLDetails, JQLSearchResult.class);

        return resolve(searchResultResponse);
    }
}
