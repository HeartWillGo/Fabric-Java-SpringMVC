package com.pingan.model;

import javax.persistence.*;

@Entity
@Table(name = "user", schema = "springmvcdemo", catalog = "")
public class UserEntity {
//    private int id;
//    private String nickname;
//    private String password;
//    private String firstName;
//    private String lastName;
    private String ID ;             //用户ID
    private String Name ;          //用户名字
    private int IdentificationType; // 证件类型
    private String Identification ;  //证件号码
    private int Sex ;               //性别
    private String Birthday ;        //生日
    private String BankCard ;        //银行卡号
    private String PhoneNumber ;     //手机号
    //private Collection<BlogEntity> blogsById;


//    @Column(name = "id", nullable = false)
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    @Basic
//    @Column(name = "nickname", nullable = false, length = 45)
//    public String getNickname() {
//        return nickname;
//    }
//
//    public void setNickname(String nickname) {
//        this.nickname = nickname;
//    }
//
//    @Basic
//    @Column(name = "password", nullable = false, length = 45)
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    @Basic
//    @Column(name = "first_name", nullable = true, length = 45)
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    @Basic
//    @Column(name = "last_name", nullable = true, length = 45)
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
    @Id
    @Column(name = "ID", nullable = true, length = 45)
    public String getID() {

    return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Basic
    @Column(name = "Name", nullable = true, length = 45)
    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }



    @Basic
    @Column(name = "IdentificationType", nullable = true, length = 45)
    public int  getIdentificationType() {
        return IdentificationType;
    }

    public void setIdentificationType(int IdentificationType) {
        this.IdentificationType = IdentificationType;
    }

    @Basic
    @Column(name = "Identification", nullable = true, length = 45)
    public String  getIdentification() {
        return Identification;
    }

    public void setIdentification(String Identification) {
        this.Identification = Identification;
    }

    @Basic
    @Column(name = "Sex", nullable = true, length = 45)
    public int  getSex() {
        return Sex;
    }

    public void setSex(int Sex) {
        this.Sex = Sex;
    }

    @Basic
    @Column(name = "Birthday", nullable = true, length = 45)
    public String  getBirthday() {
        return Birthday;
    }

    public void setBirthday(String Birthday) {
        this.Birthday = Birthday;
    }
    @Basic
    @Column(name = "BankCard", nullable = true, length = 45)
    public String  getBankCard() {
        return BankCard;
    }

    public void setBankCard(String BankCard) {
        this.BankCard = BankCard;
    }

    @Basic
    @Column(name = "PhoneNumber", nullable = true, length = 45)
    public String  getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (ID!= that.ID) return false;
        if (Name != null ? !Name.equals(that.Name) : that.Name != null) return false;
        if (IdentificationType != 0 ? IdentificationType!=that.IdentificationType: that.IdentificationType != 0) return false;
        if (Identification != null ? !Identification.equals(that.Identification) : that.Identification != null) return false;
        if (Birthday != null ? !Birthday.equals(that.Birthday) : that.Birthday != null) return false;
        if (Sex != 0 ? Sex!=that.Sex: that.Sex != 0) return false;
        if (BankCard != null ? !BankCard.equals(that.BankCard) : that.BankCard != null) return false;
        if (PhoneNumber != null ? !PhoneNumber.equals(that.PhoneNumber) : that.PhoneNumber != null) return false;

        return true;
    }

//    @Override
//    public int hashCode() {
//        int result = id;
//        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
//        result = 31 * result + (password != null ? password.hashCode() : 0);
//        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
//        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
//        return result;
//    }
    @Override
    public int hashCode() {
        int result = Integer.valueOf(ID);
        result = 31 * result + (Name != null ? Name.hashCode() : 0);
        result = 31 * result + (IdentificationType != 0 ? IdentificationType : 0);
        result = 31 * result + (Identification != null ? Identification.hashCode() : 0);
        result = 31 * result + (Birthday != null ? Birthday.hashCode() : 0);
        result = 31 * result + (Sex != 0 ? Sex : 0);
        result = 31 * result + (BankCard != null ? BankCard.hashCode() : 0);
        result = 31 * result + (PhoneNumber != null ? PhoneNumber.hashCode() : 0);
        return result;
    }

//    @OneToMany(mappedBy = "userByUserId")
//    public Collection<BlogEntity> getBlogsById() {
//        return blogsById;
//    }
//
//    public void setBlogsById(Collection<BlogEntity> blogsById) {
//        this.blogsById = blogsById;
//    }
}
