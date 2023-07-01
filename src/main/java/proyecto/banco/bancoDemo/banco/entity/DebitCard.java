package proyecto.banco.bancoDemo.banco.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@NoArgsConstructor
@Setter
@Document(collection = "debitCards")
public class DebitCard {
    @Id
    private ObjectId id;
    @Field("cardNumber")
    @Indexed(unique = true)
    private String cardNumber;
    private int securityNumber;
    private List<String> numberBankAccounts;
    private LocalDateTime created;
    private LocalDateTime updated;
    public DebitCard(ObjectId id, String cardNumber, String bankAccount) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.securityNumber = new Random().nextInt(9000) + 1000;

        this.numberBankAccounts = new ArrayList<>();
        this.numberBankAccounts.add(bankAccount);
        this.created = LocalDateTime.now();
    }
    public void setBankAccounts( List<String> bankAccountList, String bankAccountNew) {
        boolean encontrado = bankAccountList.stream()
                .anyMatch(bankAccount -> bankAccount.equals(bankAccountNew));
        if(!encontrado)bankAccountList.add(bankAccountNew);

        this.numberBankAccounts = bankAccountList;
    }


}
