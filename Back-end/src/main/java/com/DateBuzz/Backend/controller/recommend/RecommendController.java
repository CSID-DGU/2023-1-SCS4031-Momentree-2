package com.DateBuzz.Backend.controller.recommend;

import com.DateBuzz.Backend.controller.responseDto.RecordResponseDto;
import com.DateBuzz.Backend.controller.responseDto.Response;
import com.DateBuzz.Backend.exception.DateBuzzException;
import com.DateBuzz.Backend.exception.ErrorCode;
import com.DateBuzz.Backend.model.entity.UserEntity;
import com.DateBuzz.Backend.repository.UserRepository;
import com.DateBuzz.Backend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class RecommendController {

    private final RecommendService recommendService;

    private final UserRepository userRepository;

    @GetMapping("/recommendation")
    public Response<List<RecordResponseDto>> getRecommendation(Authentication authentication) {
        UserEntity user = userRepository
                .findByUserName(authentication.getName())
                .orElseThrow(() -> new DateBuzzException(ErrorCode.USER_NOT_FOUND, String.format("%s is not found", authentication.getName())));

        String currentPath = System.getProperty("user.dir");
        String scriptPath = currentPath + "/python/main.py";

        return Response.success(recommendService.getRecommendation(RecommendController.callPythonScript(scriptPath, String.valueOf(user.getId()))));
    }


    public static String callPythonScript(String scriptPath, String userId) {
        StringBuilder result = new StringBuilder();
        ProcessBuilder processBuilder = new ProcessBuilder("python3", scriptPath, userId);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            while ((line = stderrReader.readLine()) != null) {
                System.err.println("Error: " + line);
            }
            reader.close();
            stderrReader.close();
        } catch (Exception e) {
            throw new DateBuzzException(ErrorCode.PYTHON_READING_PROBLEM);
        }
        return result.toString();
    }
}
