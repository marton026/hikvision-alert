package com.marton.hikalert.controllers;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.springframework.stereotype.Component;

@Component
public class ModemConnection {

    byte newLine = 0x0D;
    byte endOfLine = 0x1A;

    SerialPort serialPort;

    public ModemConnection() throws SerialPortException {
        this.serialPort = new SerialPort("COM11");
        serialPort.openPort();
        serialPort.setParams(SerialPort.BAUDRATE_9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);

    }

    public String readSms(String memoryPlace) {
        try {
            serialPort.writeString("AT+CMGF=1");
            Thread.sleep(1000);
            serialPort.writeByte(newLine);
            Thread.sleep(1000);
            serialPort.writeString("AT+CMGR=" + memoryPlace);
            Thread.sleep(1000);
            serialPort.writeByte(newLine);
            Thread.sleep(1000);
            return serialPort.readString();

        } catch (SerialPortException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }


    public void sendSMS(String telNo, String message) {
        try {
            serialPort.writeString("AT+CMGF=1");
            serialPort.writeByte(newLine);
            Thread.sleep(1000);
            serialPort.writeString("AT+CMGS=\"" + telNo + "\"");
            serialPort.writeByte(newLine);
            Thread.sleep(1000);
            serialPort.writeString(message);
            Thread.sleep(1000);
            serialPort.writeByte(newLine);
            Thread.sleep(1000);
            serialPort.writeByte(endOfLine);

        } catch (SerialPortException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

