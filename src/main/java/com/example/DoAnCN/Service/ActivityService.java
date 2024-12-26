package com.example.DoAnCN.Service;

import com.example.DoAnCN.Entity.Activity;
import com.example.DoAnCN.Repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    public Activity saveActivity(Activity activity){
        return activityRepository.save(activity);
    }

    public Optional<Activity> findById(Long id) {
        return activityRepository.findById(id);
    }

    public Activity updateActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    public void deleteActivity(Long id) {
        activityRepository.deleteById(id);
    }
}
