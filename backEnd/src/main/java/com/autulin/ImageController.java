package com.autulin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class ImageController {

    @Autowired
    private WebCamManager webCamManager;

    @GetMapping("pictures")
    @ResponseBody
    public Response getPictures() {
        Response<List<Picture>> response = new Response<>();
        response.setCode(0);
        response.setMsg("ok");
        response.setT(webCamManager.getPictures());
        return response;
    }

    @GetMapping("picture")
    @ResponseBody
    public Response getOnePicture() {
        Response<Picture> response = new Response<>();
        response.setCode(0);
        response.setMsg("ok");
        response.setT(webCamManager.getOnePicture());
        return response;
    }

    @DeleteMapping("pictures")
    @ResponseBody
    public Response deleteAll() {
        webCamManager.clearPictures();

        Response<String> response = new Response<>();
        response.setCode(0);
        response.setMsg("ok");
        response.setT("");
        return response;
    }

    @GetMapping("index")
    public String index(Map<String, Object> model) {
        model.put("one", webCamManager.getOnePicture());
        model.put("pictures", webCamManager.getPictures());
        return "index";
    }


    public class Response<T> {
        private int code;
        private String msg;
        private T data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public T getT() {
            return data;
        }

        public void setT(T t) {
            this.data = t;
        }
    }
}
