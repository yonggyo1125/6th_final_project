package org.sparta.monitoringserver;

import org.junit.jupiter.api.Test;
import org.sparta.monitoringserver.agent.infrastructure.tool.AgentTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MonitoringServerApplicationTests {

    @Autowired
    AgentTools agentTools;

    @Test
    void contextLoads() {
        agentTools.sendDetailedSlackReport("1", "2", "3", "4");
    }

}
