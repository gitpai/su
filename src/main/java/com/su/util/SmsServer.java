package com.su.util;


import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;

public class SmsServer {
   
    public static void  sendSmsCode(String PhoneNum,String smsCode){
    	  /**
         * Step 1. ��ȡ��������
         */
        CloudAccount account = new CloudAccount("LTAICXDB8Bw3CNqw", "Xz0vMz6xKuZ6qJ1gMtk3ukcMW3VYiM", "http://1138897942461090.mns.cn-hangzhou.aliyuncs.com/");
        MNSClient client = account.getMNSClient();
        CloudTopic topic = client.getTopicRef("sms.topic-cn-hangzhou");
        /**
         * Step 2. ����SMS��Ϣ�壨���룩
         *
         * ע��Ŀǰ��ʱ��֧����Ϣ����Ϊ�գ���Ҫָ����Ϣ���ݣ���Ϊ�ռ��ɡ�
         */
        RawTopicMessage msg = new RawTopicMessage();
        msg.setMessageBody("sms-message");
        /**
         * Step 3. ����SMS��Ϣ����
         */
        MessageAttributes messageAttributes = new MessageAttributes();
        BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
        // 3.1 ���÷��Ͷ��ŵ�ǩ����SMSSignName��
        batchSmsAttributes.setFreeSignName("SHU������ɡ");
        // 3.2 ���÷��Ͷ���ʹ�õ�ģ�壨SMSTempateCode��
        batchSmsAttributes.setTemplateCode("SMS_71650094");
        // 3.3 ���÷��Ͷ�����ʹ�õ�ģ���в�����Ӧ��ֵ���ڶ���ģ���ж���ģ�û�п��Բ������ã�
        BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
        smsReceiverParams.setParam("smscode", smsCode);
     //   smsReceiverParams.setParam("$YourSMSTemplateParamKey2", "$value2");
        // 3.4 ���ӽ��ն��ŵĺ���
        batchSmsAttributes.addSmsReceiver(PhoneNum, smsReceiverParams);
       // batchSmsAttributes.addSmsReceiver("$YourReceiverPhoneNumber2", smsReceiverParams);
        messageAttributes.setBatchSmsAttributes(batchSmsAttributes);
        try {
            /**
             * Step 4. ����SMS��Ϣ
             */
            TopicMessage ret = topic.publishMessage(msg, messageAttributes);
            System.out.println("MessageId: " + ret.getMessageId());
            System.out.println("MessageMD5: " + ret.getMessageBodyMD5());
        } catch (ServiceException se) {
            System.out.println(se.getErrorCode() + se.getRequestId());
            System.out.println(se.getMessage());
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.close();
    }
}