package bookrental;

import javax.persistence.*;

import bookrental.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

@Entity
@Table(name="Delivery_table")
public class Delivery {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long orderid;
    private String userid;
    private String bookid;
    private String status;

    @PostPersist
    public void onPostPersist(){
        System.out.println("==============onPostPersist ====== bookid : "+getBookid());
        Deliverystarted deliverystarted = new Deliverystarted();
        deliverystarted.setOrderid(this.getOrderid());
        deliverystarted.setUserid(this.getUserid());
        deliverystarted.setBookid(this.getBookid());
        deliverystarted.setStatus("DeliveryStarted");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(deliverystarted);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }


        KafkaProcessor processor = Application.applicationContext.getBean(KafkaProcessor.class);
        MessageChannel outputChannel = processor.outboundTopic();

        outputChannel.send(MessageBuilder
                .withPayload(json)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());


        //BeanUtils.copyProperties(this, incomed);
        //incomed.publishAfterCommit();

    }

    @PostUpdate
    public void onPostUpdate(){
        System.out.println("==============onPostPersist ====== bookid : "+getBookid());
        Deliverycompleted deliverycompleted = new Deliverycompleted();
        deliverycompleted.setOrderid(this.getOrderid());
        deliverycompleted.setUserid(this.getUserid());
        deliverycompleted.setBookid(this.getBookid());
        deliverycompleted.setStatus(this.getStatus());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(deliverycompleted);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }


        KafkaProcessor processor = Application.applicationContext.getBean(KafkaProcessor.class);
        MessageChannel outputChannel = processor.outboundTopic();

        outputChannel.send(MessageBuilder
                .withPayload(json)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());


        //BeanUtils.copyProperties(this, incomed);
        //incomed.publishAfterCommit();

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getOrderid() {
        return orderid;
    }

    public void setOrderid(Long orderid) { this.orderid = orderid; }
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}
