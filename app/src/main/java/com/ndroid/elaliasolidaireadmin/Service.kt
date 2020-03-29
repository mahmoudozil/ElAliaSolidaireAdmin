package com.ndroid.elaliasolidaire

import java.io.Serializable

class Service() : Serializable{

    var user:String = ""
    var adresse:String = ""
    var tel:String = ""
    var service:String = ""
    var dateDemande:String = ""
    var databaseKey:String = ""
    var etat:Int = 0
    var userKey:String = ""
    var volunteer: String = ""

    constructor(user: String, adresse: String, tel: String, service: String, dateDemande: String, databaseKey: String, etat: Int, userKey:String, volunteer: String) : this() {
        this.user = user
        this.adresse = adresse
        this.tel = tel
        this.service = service
        this.dateDemande = dateDemande
        this.databaseKey = databaseKey
        this.etat = etat
        this.userKey = userKey
        this.volunteer = volunteer
    }

    constructor(user: String, adresse: String, tel: String, service: String) : this() {
        this.user = user
        this.adresse = adresse
        this.tel = tel
        this.service = service
    }

}
