package com.example.webhookapp.service;

import com.example.webhookapp.model.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void executeFlow() {

        try {

            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

            String jsonBody = """
            {
              "name": "Yash Kumar",
              "regNo": "ADT23SOCB1336",
              "email": "yash@gmail.com"
            }
            """;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<GenerateResponse> response =
                    restTemplate.postForEntity(url, entity, GenerateResponse.class);

            GenerateResponse body = response.getBody();

            if (body == null) {
                System.out.println("Response body is null");
                return;
            }

            String webhookUrl = body.getWebhook();
            String token = body.getAccessToken();

            System.out.println("Webhook: " + webhookUrl);
            System.out.println("Token: " + token);

            // ✅ FIX: Extract last 2 digits dynamically
            String regNo = "ADT23SOCB1336";
            int lastTwoDigits = Integer.parseInt(regNo.substring(regNo.length() - 2));

            String finalQuery;

            if (lastTwoDigits % 2 == 0) {
                finalQuery = solveQuestion2(); // EVEN
            } else {
                finalQuery = solveQuestion1(); // ODD
            }

            submitAnswer(webhookUrl, token, finalQuery);

        } catch (Exception e) {
            System.out.println("ERROR OCCURRED:");
            e.printStackTrace();
        }
    }

    private void submitAnswer(String url, String token, String query) {

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", token);

            SubmitRequest request = new SubmitRequest(query);

            HttpEntity<SubmitRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, entity, String.class);

            System.out.println("Final API Response: " + response.getBody());

        } catch (Exception e) {
            System.out.println("ERROR IN SUBMIT:");
            e.printStackTrace();
        }
    }

    // ✅ YOU ARE EVEN → This will NOT run
    private String solveQuestion1() {
        return "SELECT 1"; // dummy (not used)
    }

    // ✅ YOUR ACTUAL QUERY (EVEN case)
    private String solveQuestion2() {
        return "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT FROM EMPLOYEE e1 JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT AND e2.DOB > e1.DOB GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME ORDER BY e1.EMP_ID DESC;";
    }
}