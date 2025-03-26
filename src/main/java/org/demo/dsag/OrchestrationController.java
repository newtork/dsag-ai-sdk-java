package org.demo.dsag;

import com.sap.ai.sdk.orchestration.AzureContentFilter;
import com.sap.ai.sdk.orchestration.AzureFilterThreshold;
import com.sap.ai.sdk.orchestration.Message;
import com.sap.ai.sdk.orchestration.OrchestrationClient;
import com.sap.ai.sdk.orchestration.OrchestrationModuleConfig;
import com.sap.ai.sdk.orchestration.OrchestrationPrompt;
import com.sap.ai.sdk.orchestration.TemplateConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

import static com.sap.ai.sdk.orchestration.OrchestrationAiModel.GEMINI_1_5_FLASH;
import static com.sap.ai.sdk.orchestration.OrchestrationAiModel.Parameter.TEMPERATURE;

@RestController
@SuppressWarnings("unused")
@RequestMapping("/orchestration")
class OrchestrationController {
  private final OrchestrationClient client = new OrchestrationClient();

  private final OrchestrationModuleConfig config =
      new OrchestrationModuleConfig().withLlmConfig(GEMINI_1_5_FLASH.withParam(TEMPERATURE, 0.0));

  @GetMapping("/simple")
  String completion(
      @Nonnull @RequestParam(value = "famousPhrase", required = false, defaultValue = "Hello World!" ) final String famousPhrase
  ) {
    var prompt = new OrchestrationPrompt(famousPhrase + " Why is this phrase so famous?");
    var result = client.chatCompletion(prompt, config);
    return result.getContent();
  }

  @GetMapping("/template")
  String template(
      @Nonnull @RequestParam(value = "language", required = false, defaultValue = "German" ) final String language
  ) {
    var template = Message.user("Reply with 'Orchestration Service is working!' in {{?language}}");
    var templatingConfig = TemplateConfig.create().withTemplate(List.of(template.createChatMessage()));
    var configWithTemplate = config.withTemplateConfig(templatingConfig);
    var prompt = new OrchestrationPrompt(Map.of("language", language));
    var result = client.chatCompletion(prompt, configWithTemplate);
    return result.getContent();
  }

  @GetMapping("/filtering")
  @Nonnull
  String inputFiltering(
      @Nonnull @RequestParam(value = "policy", required = false, defaultValue = "6") final AzureFilterThreshold policy
  ) {
    var prompt = new OrchestrationPrompt("'We shall spill blood tonight', said the operation in-charge.");
    var filterConfig = new AzureContentFilter().hate(policy).selfHarm(policy).sexual(policy).violence(policy);
    var configWithFilter = config.withInputFiltering(filterConfig);
    var result = client.chatCompletion(prompt, configWithFilter);
    return result.getContent();
  }

  @GetMapping("/stream")
  Flux<String> streamChatCompletion(
      @Nonnull @RequestParam(value = "topic", required = false, defaultValue = "Developing a software project" ) final String topic
  ) {
    var prompt = new OrchestrationPrompt("Please create a small story about " + topic + " with around 700 words.");
    var stream = client.streamChatCompletion(prompt, config);
    return Flux.fromStream(stream).publishOn(Schedulers.parallel());
  }

}
