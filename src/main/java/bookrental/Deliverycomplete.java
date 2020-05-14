package bookrental;

public class Deliverycomplete extends AbstractEvent {

    private Long id;

    public Deliverycomplete(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
