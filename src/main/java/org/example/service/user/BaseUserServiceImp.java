package org.example.service.user;

import lombok.SneakyThrows;
import org.example.domain.PassAndUser;
import org.example.domain.User;
import org.example.dto.ChangingPasswordDto;
import org.example.exeptions.AllNotBeLetterOrDigits;
import org.example.exeptions.PassNot8Digits;

import java.util.function.Predicate;

public abstract class BaseUserServiceImp <T extends User> implements BaseUserService<T> {
    BaseUserRepository baseUserRepository = new baseUserRepositoryImp();

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
    public void signUp(T t){
        baseUserRepository.save(t);
    }
    @Override
    public void savePassAndUser(PassAndUser passAndUser){
        baseUserRepository.saveUserAndPass();
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
    public void changingPassword(ChangingPasswordDto changingPasswordDto){
        PassAndUser passAndUser = PassAndUser.builder().username(changingPasswordDto.user()).typeOfUser(changingPasswordDto.typeOfUser()).pass(changingPasswordDto.oldPass()).build();
        PassAndUser newPassAndUser = baseUserRepository.findPassAndUser(passAndUser);
        if(newPassAndUser != null){
        newPassAndUser.setPass(changingPasswordDto.newPass());
        baseUserRepository.updatePass(newPassAndUser);
        }
    }
    @Override
    public void updateUser(T t){
        baseUserRepository.update(t);
    };

}
