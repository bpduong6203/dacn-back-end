package com.example.DoAnCN.Controller;

import com.example.DoAnCN.Entity.Activity;
import com.example.DoAnCN.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @PostMapping("/create")
    public ResponseEntity<Activity> create(@RequestBody Activity activity) {
        Activity save = activityService.saveActivity(activity);
        return ResponseEntity.ok(save);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Activity activityDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Activity activity = activityService.findById(id).orElseThrow(() -> new RuntimeException("Activity not found"));
            activity.setActivity_name(activityDetails.getActivity_name());
            activity.setStart_time(activityDetails.getStart_time());
            activity.setEnd_time(activityDetails.getEnd_time());
            activity.setItinerary(activityDetails.getItinerary());
            Activity updatedActivity = activityService.saveActivity(activity);
            response.put("message", "Activity updated successfully");
            response.put("activity", updatedActivity);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            response.put("error", "Failed to update activity: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }
}
