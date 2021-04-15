package furhatos.app.project.flow
import furhatos.app.project.nlu.pluralsingle
import furhatos.nlu.SimpleIntent
import java.io.File

/*
This tab contains all functions used in the interaction tab
 */

val uiterlijk = listOf("Beard", "Mustache", "Glasses", "BigLips")
//the tag _ONO will be replaced with the correct trait later on
val singleCompl = listOf("You have such a pretty _ONO",
    "Your _ONO is amazing",
    "I can’t get over how beautiful your _ONO is",
    "Your _ONO is mesmerizing",
    "Do you know how beautiful your _ONO is?")
val pluralCompl = listOf("You have such pretty _ONO",
    "Your _ONO are amazing",
    "I can’t get over how beautiful your _ONO are",
    "Your _ONO are mesmerizing",
    "Do you know how beautiful your _ONO are?")
val neutralCompl = listOf(
    "I love the colour of your _ONO",
    "You have the prettiest _ONO I have ever seen",
    "I just can't get over your wonderful _ONO")

val otherCompl = listOf("I may be just 1s and 0s but you are an absolute 10",
    "You make my FurHeart beat faster _wink", //furhat needs to wink
    "I'll save you when robots take over the world")
var topic = uiterlijk.random()
var lastTopic = ""
var utteredTopic = ""
var pluralID = pluralsingle(topic)
var compList: List<String>? = null
var compSent: String = "test"

fun <T> merge(first: List<T>, second: List<T>): List<T> {
    return first + second
}

fun reset(): Void?{
    lastTopic = utteredTopic
    topic = ""
    pluralID = 0
    compList = null
    compSent = ""
    return null
}

fun createCompl(): Void? {
    var i = 0
    var valid = false
    var topicindex = 0
    while (true){
        valid = false
        topic = getFeat(attList.random())[0]
        topicindex = attList.indexOf(topic)
        pluralID = pluralsingle(topic)
        println(topic + " " + pluralID)
        if(getFeat(topic)[2] != " yes" && getFeat(topic)[2] != "yes" && getFeat(topic)[2] != " white"){
        //if(getFeat(topic)[2] != " no" && getFeat(topic)[2] != "no"){
                println("invalid")
                i++
                if(i == 50){
                    compSent = otherCompl.random()
                    //at this point FurHat will just give an "other" compliment.
                    return null
                }
                continue
            } else{
                println("valid")
                valid = true
                //FurHat has found a valid compliment and will use this
            }

        i++
        if(valid == true && (pluralID == 1 || pluralID == 2)){
            break
        } else if(i == 50){
            compSent = otherCompl.random()
            //at this point Furhat will jjust give an "other" compliment.
            return null
        }
    } //at this point; FH has a topic he can use and knows whether it;s plural or sinlguar
    if (pluralID == 1){
        compList = merge(singleCompl, neutralCompl)
    } else if(pluralID == 2){
        compList = merge(pluralCompl, neutralCompl)
    } else { //This should never be reached
        println("Everything is bad, please stop!")
    } //compList is generated and contains the correct compliment wrt to the plural or single element

    utteredTopic = attListsmall[topicindex]
    lastTopic = utteredTopic
    compSent = compList!!.random().replace("_ONO", utteredTopic)
    //compSent = compSent.replace("wearing", "")
    println("Compliment successfully generated: ")
    println(compSent)
    return null
}

//replace a certain word with a different word
//      val baard = "beard looks _ONO"
//      "${baard.replace("_ONO", "bad")}"

var exFeatList = listOf<String>()
val attList = listOf("FiveOClockShadow", "Age", "ArchedEyebrows", "Attractive", "BagsUnderEyes", "Bald", "Bangs", "Beard", "BigLips", "BigNose", "BlackHair", "BlondHair", "Blurry", "BrownHair", "BushyEyebrows", "Chubby", "DoubleChin", "Expression", "Gender", "Glasses", "Goatee", "GrayHair", "HeavyMakeUp", "HighCheekbones", "MouthOpen", "Mustache", "NarrowEyes", "OvalFace", "PaleSkin", "Pitch", "PointyNose", "Race", "RecedingHairline", "RosyCheeks", "Sideburns", "StraightHair", "WavyHair", "Earrings", "Hat", "Lipstick", "Necklace", "Necktie", "Yaw", "Young")
val attListsmall = listOf("five 'o clock shadow", "age", "arched eyebrows", "attractive", "bags under eyes", "bald", "bangs", "beard", "big lips", "big nose", "black hair", "blond hair", "blurry", "brown hair", "bushy eyebrows", "chubby", "double chin", "expression", "gender", "glasses", "goatee", "gray hair", "heavy make up", "high cheekbones", "mouth open", "moustache", "narrow eyes", "oval face", "pale skin", "pitch", "pointy nose", "race", "receding hairline", "rosy cheeks", "sideburns", "straight hair", "wavy hair", "earrings", "hat", "lipstick", "necklace", "necktie", "yaw", "young")
//use the attListsmall to use the correct interpunction types
//val attResponse = SimpleIntent("five 'o clock shadow", "age", "arched eyebrows", "attractive", "bags under eyes", "bald", "bangs", "beard", "big lips", "big nose", "black hair", "blond hair", "blurry", "brown hair", "bushy eyebrows", "chubby", "double chin", "expression", "gender", "glasses", "goatee", "gray hair", "heavy make up", "high cheekbones", "mouth open", "mustache", "narrow eyes", "oval face", "pale skin", "pitch", "pointy nose", "race", "receding hairline", "rosy cheeks", "sideburns", "straight hair", "wavy hair", "earrings", "hat", "lipstick", "necklace", "necktie", "yaw", "young")


//This functions needs to run only once.
//It imports the list of variables and puts it into a long string
fun readFileVar(fileName: String): Void?{
    val path = System.getProperty("user.dir")
    println("Working Dir = $path")
    var p = 0
    File(fileName).forEachLine{
        // val (attribute, name, yes/no/X, confidence)
        var parts = it.split(",")
        var t = mutableListOf<String>("", "", "", "")
        t[0] = attList[p]
        for(j in parts.indices)
        {
            t[j+1] = parts[j]
        }
        exFeatList += t
        p++
    }
    return null
}

fun getFeat(feat : String): List<String> {
    var x = attList.indexOf(feat)
    var y = x*4
    return listOf(exFeatList[y], exFeatList[y+1],exFeatList[y+2],exFeatList[y+3])
}

fun otherAttribute(attr : String? = null): Int{
    var findPlace = attListsmall.indexOf(attr)
    var correctPlace = attList[findPlace]
    var plsi = pluralsingle(correctPlace)
    println("Location of "
        +attr
        +" is "
        +findPlace)
        return plsi

}