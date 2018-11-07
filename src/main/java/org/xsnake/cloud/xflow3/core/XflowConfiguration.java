package org.xsnake.cloud.xflow3.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xsnake.cloud.xflow3.api.Participant;

@Configuration
@RefreshScope
public class XflowConfiguration {

	@Value(value = "${xflow3.emptyParticipant.id}")
	String emptyParticipantId;
	
	@Value(value = "${xflow3.emptyParticipant.name}")
	String emptyParticipantName;
	
	@Value(value = "${xflow3.emptyParticipant.type}")
	String emptyParticipantType;

	@Bean
	public Participant emptyParticipant(){
		return new Participant(emptyParticipantId,emptyParticipantName,emptyParticipantType);
	}
	
}
