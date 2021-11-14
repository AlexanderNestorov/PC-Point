package com.example.pcpoint.service.stats;

import com.example.pcpoint.model.entity.stat.StatsView;

public interface StatsService {

    void onRequest();
    StatsView getStats();
}
