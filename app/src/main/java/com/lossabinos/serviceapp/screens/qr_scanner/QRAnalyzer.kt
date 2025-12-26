package com.lossabinos.serviceapp.screens.qr_scanner

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class QRAnalyzer(
    private val onQRScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {

    // 🆕 Configurar ML Kit para QR codes
    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)  // Solo QR
        .build()

    private val scanner = BarcodeScanning.getClient(options)
    private var lastScannedValue: String? = null
    private var lastScannedTime: Long = 0

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image

        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        // 🆕 Obtener valor del QR
                        val qrValue = barcode.rawValue

                        if (qrValue != null && qrValue != lastScannedValue) {
                            // Evitar múltiples escaneos del mismo valor
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastScannedTime > 1000) {  // 1 segundo
                                println("✅ QR Escaneado: $qrValue")
                                lastScannedValue = qrValue
                                lastScannedTime = currentTime

                                // 🆕 Retornar al callback
                                onQRScanned(qrValue)
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    println("❌ Error procesando QR: ${e.message}")
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}
