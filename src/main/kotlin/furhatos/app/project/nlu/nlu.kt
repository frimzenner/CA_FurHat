package furhatos.app.project.nlu

import furhatos.nlu.*

import furhatos.nlu.grammar.Grammar
import furhatos.nlu.kotlin.grammar
import furhatos.nlu.common.Number
import furhatos.util.Language

//class AttResponse (val att : AttResponseList? = null): Intent()
class AttResponseList : EnumEntity()

//Takes the list of compliments and uses this for an Intent (User response)
class Compliments : Intent()

fun pluralsingle(dummy: String) : Int{
    when(dummy){
        "FiveOClockShadow" -> return 1
        "Age" -> return 3
        "ArchedEyebrows", "BushyEyebrows" -> return 2
        "Attractive" -> return 3
        "BagsUnderEyes" -> return 2
        "Bald" -> return 3
        "Bangs" -> return 2
        "Beard" -> return 1
        "BigLips" -> return 2
        "BigNose", "PointyNose" -> return 1
        "BlackHair", "BlondHair", "BrownHair", "GrayHair" -> return 1
        "Blurry" -> return 3
        "Chubby" -> return 3
        "DoubleChin" -> return 1
        "Expression" -> return 3
        "Gender" -> return 3
        "Glasses" -> return 2
        "Goatee" -> return 1
        "HeavyMakeUp" -> return 1
        "HighCheekbones" -> return 2
        "MouthOpen" -> return 3
        "Mustache" -> return 1
        "NarrowEyes" -> return 2
        "OvalFace" -> return 1
        "PaleSkin" -> return 1
        "Pitch" -> return 3
        "Race" -> return 1
        "RecedingHairline" -> return 1
        "RosyCheeks" -> return 2
        "Sideburns" -> return 2
        "StraightHair", "WavyHair" -> return 1
        "Earrings" -> return 2
        "Hat" -> return 1
        "Lipstick" -> return 1
        "Necklace" -> return 1
        "Necktie" -> return 1
        "Yaw" -> return 3
        "Young" -> return 3

        else -> {return 0
            println("When x does not belong to any of the above case")}
    }
}