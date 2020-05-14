package bookrental;

import bookrental.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired
    DeliveryRepository deliveryRepository;
    
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReserved_Deliveryrequest(@Payload Reserved reserved){
        if (reserved.isMe()) {
            Delivery delivery = new Delivery();
            delivery.setOrderid(reserved.getId());
            delivery.setUserid(reserved.getUserid());
            delivery.setBookid(reserved.getBookid());
            deliveryRepository.save(delivery);
        }
    }

}
