package org.example.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.domain.Employee;
import org.example.domain.Orders;
import org.example.domain.PassAndUser;
import org.example.domain.Users;
import org.example.dto.ChangingPasswordDto;
import org.example.enumirations.TypeOfUser;
import org.example.exeptions.*;
import org.example.repository.user.BaseUserRepository;
import org.example.repository.user.BaseUserRepositoryImp;
import org.example.service.emailToken.EmailTokenService;
import org.example.service.mapStruct.EntityMapper;
import org.example.service.offer.OfferService;
import org.example.service.order.OrderService;
import org.example.service.subHandler.SubHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



public abstract class BaseUserServiceImp <T extends Users> implements BaseUserService<T> {
    private final BaseUserRepository baseUserRepository ;
    protected final OrderService orderService ;
    protected final OfferService offerService ;
    protected final SubHandlerService subHandlerService;
    protected final EntityMapper entityMapper;
    protected final EmailTokenService emailTokenService ;
    @Autowired
    public BaseUserServiceImp(BaseUserRepository baseUserRepository,OrderService orderService,OfferService offerService,SubHandlerService subHandlerService,EntityMapper entityMapper,EmailTokenService emailTokenService ){
        this.baseUserRepository = baseUserRepository;
        this.orderService = orderService;
        this.offerService = offerService;
        this.subHandlerService = subHandlerService;
        this.entityMapper = entityMapper;
        this.emailTokenService = emailTokenService;
    }
    @SneakyThrows
    @Override
    public boolean validatePassWord(String pass) {
        String pass1 = pass.replaceAll("\\s","");
        //todo: check the password and validate it
        if (checkIfIts8Digits(pass) && checkIfAllNotBeLetterOrDigit(pass))
        return true;
        else {
            return false;
        }
    }
    @Override
    @Transactional
    public void signUp(T t){
        baseUserRepository.save(t);
    }
    @Override
    @Transactional
    public void savePassAndUser(PassAndUser passAndUser){
        baseUserRepository.saveUserAndPass(passAndUser);
    }

    private boolean checkIfIts8Digits(String pass) throws PassNot8Digits{
        if (pass.length() == 8 ){
            return true;
        }
        else {
            throw new PassNot8Digits();
        }
    }
    private boolean checkIfAllNotBeNumber(String pass) throws PassNot8Digits{
        for (char c : pass.toCharArray()){
            if (!Character.isDigit(c)){
                return true;
            }
        }
        return false;
    }
    private boolean checkIfAllNotBeLetter(String pass) throws PassNot8Digits{
        for (char c : pass.toCharArray()){
            if (!Character.isLetter(c)){
                return true;
            }
        }
        return false;
    }
    private boolean checkIfAllNotBeLetterOrDigit(String pass) throws Exception{
        if (checkIfAllNotBeLetter(pass) && checkIfAllNotBeNumber(pass)){
            return true;
        }
        else {
            throw new AllNotBeLetterOrDigits();
        }
    }
    @Override
    @Transactional
    public void changingPassword(ChangingPasswordDto changingPasswordDto){
        PassAndUser passAndUser = PassAndUser.builder().username(changingPasswordDto.user())
                .typeOfUser(changingPasswordDto.typeOfUser())
                .pass(changingPasswordDto.oldPass()).build();
        PassAndUser newPassAndUser = baseUserRepository.findPass(passAndUser);
        if(newPassAndUser != null){
        newPassAndUser.setPass(changingPasswordDto.newPass());
        baseUserRepository.updatePass(newPassAndUser);
        }else {
            throw new UnableToChangePassWord("You should enter all field correctly ");
        }
    }
    @Override
    @Transactional
    public void updateUser(T t){
        baseUserRepository.update(t);
    };
    @Override
    @Transactional
    public T findById(int id , Class<T> tClass){
            return Optional.ofNullable((T) baseUserRepository.findById(id,tClass)).orElseThrow(()-> new NotFoundUser("Unable to find Employee with this ID : "+ id));
    }

    @Override
    public List<Orders> optionalFindOrders(LocalDate startDate, LocalDate endDate, List<String> handlersName, List<String> subHandlers){
        return orderService.optionalFindOrders(startDate, endDate, handlersName, subHandlers);
    }

}
