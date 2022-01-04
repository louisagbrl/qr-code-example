/*
 * $Id$
 *
 * Copyright (c) 2018 Aero Systems Indonesia, PT.
 * All rights reserved.
 *
 * AERO SYSTEMS INDONESIA PROPRIETARY/CONFIDENTIAL. Use is subject to
 * license terms.
 */
package id.co.asyst.amala.qr.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * @author Louisa Gabriella
 * @version $Revision$, Jun 18, 2021
 * @since 4.0
 */

@RestController
@RequestMapping(path = "/amala/v1.2/qrcodegenerator")
public class XRCodeController {
    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public BufferedImage generateQRCodeImage(@RequestParam String qrText) throws Exception {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 250, 250);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    @PostMapping("/stringToImage")
    public BufferedImage stringToImage(@RequestBody Map<String, Object> request) throws WriterException {
        String qrText = request.get("qrText").toString();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 250, 250);
        BitMatrix bitMatrix = qrCodeWriter.encode("qrText", BarcodeFormat.QR_CODE, 250, 250);
//        int height = Integer.parseInt(request.get("qrSize").toString());
//        int width = Integer.parseInt(request.get("qrSize").toString());
//        String text = request.get("qrText").toString();
//        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, height, width);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    @PostMapping("/stringToBase64")
    public Map<String, Object> stringToBase64(@RequestBody Map<String, Object> request) throws WriterException, IOException {
        Map<String, Object> response = new HashMap<>();
        BitMatrix matrix = new MultiFormatWriter().encode(request.get("qrText").toString(), BarcodeFormat.QR_CODE, 250, 250);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "png", bos);
        String image = Base64.getEncoder().encodeToString(bos.toByteArray()); // base64 encode
        System.out.println(image);
        response.put("base64", image);
        return response;
    }
}

