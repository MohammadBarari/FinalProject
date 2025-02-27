package org.example.service.user;

import jakarta.transaction.Transactional;
import org.example.domain.Orders;
import org.example.domain.PassAndUser;
import org.example.domain.Users;
import org.example.dto.password.ChangingPasswordDto;
import org.example.dto.admin.FindFilteredOrdersDto;
import org.example.exeptions.NotFoundException.NotFoundUser;
import org.example.exeptions.password.AllNotBeLetterOrDigits;
import org.example.exeptions.password.PassNot8Digits;
import org.example.exeptions.password.PasswordNotCorrect;
import org.example.exeptions.password.UnableToChangePassWord;
import org.example.repository.passAndUser.PassAndUserRepository;
import org.example.repository.user.BaseUserRepository;
import org.example.service.credit.CreditService;
import org.example.service.emailToken.EmailTokenService;
import org.example.service.mapStruct.EntityMapper;
import org.example.service.offer.OfferService;
import org.example.service.order.OrderService;
import org.example.service.subHandler.SubHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;


public abstract class BaseUserServiceImp <T extends Users> implements BaseUserService<T> {

    private final BaseUserRepository baseUserRepository ;
    protected final OrderService orderService ;
    protected final OfferService offerService ;
    protected final SubHandlerService subHandlerService;
    protected final EntityMapper entityMapper;
    protected final EmailTokenService emailTokenService ;
    protected final CreditService creditService ;
    protected final PasswordEncoder passwordEncoder;
    protected final PassAndUserRepository passAndUserRepository;

    @Autowired
    public BaseUserServiceImp(BaseUserRepository baseUserRepository,PasswordEncoder passwordEncoder,CreditService creditService,OrderService orderService,OfferService offerService,SubHandlerService subHandlerService,EntityMapper entityMapper,EmailTokenService emailTokenService ,PassAndUserRepository passAndUserRepository){
        this.baseUserRepository = baseUserRepository;
        this.orderService = orderService;
        this.offerService = offerService;
        this.subHandlerService = subHandlerService;
        this.entityMapper = entityMapper;
        this.emailTokenService = emailTokenService;
        this.creditService = creditService;
        this.passwordEncoder =passwordEncoder;
        this.passAndUserRepository = passAndUserRepository;
    }


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
        passAndUserRepository.save(passAndUser);
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
    private boolean checkIfAllNotBeLetterOrDigit(String pass){
        if (checkIfAllNotBeLetter(pass) && checkIfAllNotBeNumber(pass)){
            return true;
        }
        else {
            throw new AllNotBeLetterOrDigits();
        }
    }
    @Override
    @Transactional
    public String changingPassword(ChangingPasswordDto changingPasswordDto){
        PassAndUser passAndUser = PassAndUser.builder().username(changingPasswordDto.user())
                .typeOfUser(changingPasswordDto.typeOfUser())
                .pass(passwordEncoder.encode(changingPasswordDto.oldPass())).build();
        PassAndUser newPassAndUser = Optional.ofNullable(passAndUserRepository.findPass(passAndUser.getUsername(), String.valueOf(passAndUser.getTypeOfUser()))).orElseThrow(()-> new UnableToChangePassWord());
        if (!passwordEncoder.matches(changingPasswordDto.oldPass(), passAndUser.getPass())){
            throw new PasswordNotCorrect();
        }
        newPassAndUser.setPass(passwordEncoder.encode(changingPasswordDto.newPass()));
        passAndUserRepository.save(newPassAndUser);
        return "successful";
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
    public List<Orders> optionalFindOrders(FindFilteredOrdersDto input){
        return orderService.optionalFindOrders(input.startDate(), input.endDate(), input.handlersName(), input.subHandlersName());
    }

}
