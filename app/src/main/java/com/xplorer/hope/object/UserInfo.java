package com.xplorer.hope.object;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

/**
 * Created by Raghavendra on 11-01-2015.
 */

@ParseClassName("_User")
public class UserInfo extends ParseUser {

    public UserInfo(){}

    public String getType() { return getString("type"); }

    public void setType(String type) {
        put("type", type);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getDob() {
        return getString("dob");
    }

    public void setDob(String dob) {
        put("dob", dob);
    }

    public String getGender() {
        return getString("gender");
    }

    public void setGender(String gender) {
        put("gender", gender);
    }

    public String getAddress() {
        return getString("address");
    }

    public void setAddress(String address) {
        put("address", address);
    }

    public String getPhoneNo() {
        return getString("phoneNo");
    }

    public void setPhoneNo(String phoneNo) {
        put("phoneNo", phoneNo);
    }

    //  DishWashing
    public Boolean getDishWashing() {
        return getBoolean("DishWashing");
    }

    public void setDishWashing(Boolean DishWashing) {
        put("DishWashing",DishWashing );
    }

    public long getDishWashingExpWage() {
        return getLong("DishWashingExpWage");
    }

    public void setDishWashingExpWage(long DishWashingExpWage) {
        put("DishWashingExpWage",DishWashingExpWage );
    }

    //  DishWashing
    public Boolean getCooking() {
        return getBoolean("Cooking");
    }

    public void setCooking(Boolean Cooking) {
        put("Cooking",Cooking );
    }

    public long getCookingExpWage() {
        return getLong("CookingExpWage");
    }

    public void setCookingExpWage(long CookingExpWage) {
        put("CookingExpWage",CookingExpWage );
    }

    //  HouseCleaning
    public Boolean getHouseCleaning() {
        return getBoolean("HouseCleaning");
    }

    public void setHouseCleaning(Boolean HouseCleaning) {
        put("HouseCleaning", HouseCleaning);
    }

    public long getHouseCleaningExpWage() {
        return getLong("HouseCleaningExpWage");
    }

    public void setHouseCleaningExpWage(long HouseCleaningExpWage) {
        put("HouseCleaningExpWage",HouseCleaningExpWage );
    }

    //  ClothWashing
    public Boolean getClothWashing() {
        return getBoolean("ClothWashing");
    }

    public void setClothWashing(Boolean ClothWashing) {
        put("ClothWashing",ClothWashing );
    }

    public long getClothWashingExpWage() {
        return getLong("ClothWashingExpWage");
    }

    public void setClothWashingExpWage(long ClothWashingExpWage) {
        put("ClothWashingExpWage", ClothWashingExpWage);
    }

    //  "Construction",
    public Boolean getConstruction() {
        return getBoolean("Construction");
    }

    public void setConstruction(Boolean Construction) {
        put("Construction", Construction);
    }

    public long getConstructionExpWage() {
        return getLong("ConstructionExpWage");
    }

    public void setConstructionExpWage(long ConstructionExpWage) {
        put("ConstructionExpWage",ConstructionExpWage );
    }

    //  Wallpaint
    public Boolean getWallpaint() {
        return getBoolean("Wallpaint");
    }

    public void setWallpaint(Boolean Wallpaint) {
        put("Wallpaint", Wallpaint);
    }

    public long getWallpaintExpWage() {
        return getLong("WallpaintExpWage");
    }

    public void setWallpaintExpWage(long WallpaintExpWage) {
        put("WallpaintExpWage", WallpaintExpWage);
    }

    //  Driver
    public Boolean getDriver() {
        return getBoolean("Driver");
    }

    public void setDriver(Boolean Driver) {
        put("Driver", Driver);
    }

    public long getDriverExpWage() {
        return getLong("DriverExpWage");
    }

    public void setDriverExpWage(long DriverExpWage) {
        put("DriverExpWage", DriverExpWage);
    }

    //  Guard
    public Boolean getGuard() {
        return getBoolean("Guard");
    }

    public void setGuard(Boolean Guard) {
        put("Guard", Guard);
    }

    public long getGuardExpWage() {
        return getLong("GuardExpWage");
    }

    public void setGuardExpWage(long GuardExpWage) {
        put("GuardExpWage", GuardExpWage);
    }


    //  ShopWorker

    public Boolean getShopWorker() {
        return getBoolean("ShopWorker");
    }

    public void setShopWorker(Boolean ShopWorker) {
        put("ShopWorker", ShopWorker);
    }

    public long getShopWorkerExpWage() {
        return getLong("ShopWorkerExpWage");
    }

    public void setShopWorkerExpWage(long ShopWorkerExpWage) {
        put("ShopWorkerExpWage", ShopWorkerExpWage);
    }

    //  Gardening
    public Boolean getGardening() {
        return getBoolean("Gardening");
    }

    public void setGardening(Boolean Gardening) {
        put("Gardening", Gardening);
    }

    public long getGardeningExpWage() {
        return getLong("GardeningExpWage");
    }

    public void setGardeningExpWage(long GardeningExpWage) {
        put("GardeningExpWage",GardeningExpWage );
    }

    //  Miscellaneous
    public Boolean getMiscellaneous() {
        return getBoolean("Miscellaneous");
    }

    public void setMiscellaneous(Boolean Miscellaneous) {
        put("Miscellaneous",Miscellaneous );
    }

    public long getMiscellaneousExpWage() {
        return getLong("MiscellaneousExpWage");
    }

    public void setMiscellaneousExpWage(long MiscellaneousExpWage) {
        put("MiscellaneousExpWage", MiscellaneousExpWage);
    }


    //  LangR
    public Boolean getLangEnglish() {
        return getBoolean("LangEnglish");
    }

    public void setLangEnglish(Boolean LangEnglish) {
        put("LangEnglish", LangEnglish);
    }
    public Boolean getLangHindi() {
        return getBoolean("LangHindi");
    }

    public void setLangHindi(Boolean LangHindi) {
        put("LangHindi", LangHindi);
    }

    //  licenseType
    public Boolean getlicenseFour() {
        return getBoolean("licenseType");
    }

    public void setlicenseFour(Boolean licenseFour) {
        put("licenseFour", licenseFour);
    }
    public Boolean getlicenseHeavy() {
        return getBoolean("licenseHeavy");
    }

    public void setlicenseHeavy(Boolean licenseHeavy) {
        put("licenseHeavy", licenseHeavy);
    }


    public ParseFile getImageFile() {
        return getParseFile("image");
    }

    public void setImageFile(ParseFile image) {
        put("image", image);
    }


}
