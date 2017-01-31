package com.tsimura.web.controller.vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.queries.users.UserField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.vkontakte.api.VKontakte;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VkProfileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VkProfileController.class);

    @Autowired
    private ConnectionRepository connectionRepository;

    @GetMapping("/vk")
    public String vk(Model model) throws ClientException, ApiException {
        LOGGER.debug("I'm inside vk controller");

        Connection<VKontakte> connection = connectionRepository.findPrimaryConnection(VKontakte.class);
        if (connection == null) return "redirect:/connect/vk";

        VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());
        model.addAttribute("profile", vk.users()
                .get(connection.getApi().getUserActor())
                .fields(UserField.SCREEN_NAME).execute().get(0));

        model.addAttribute("email", connection.getApi().getEmail());
        return "vk/profile";
    }
}
