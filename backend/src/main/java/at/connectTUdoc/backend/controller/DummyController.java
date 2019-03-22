package at.connectTUdoc.backend.controller;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/dummy")
public class DummyController {
    
    @RequestMapping(name="/", method = RequestMethod.GET)
    @CrossOrigin // For testing from other origins (not behind gateway)
    public String dummy() {
        return "Hello World!";
    }
}
