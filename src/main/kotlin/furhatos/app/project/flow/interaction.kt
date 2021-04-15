package furhatos.app.project.flow
import java.io.File
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
//import furhatos.nlu.SimpleIntent
import furhatos.app.project.nlu.*
import kotlinx.coroutines.newFixedThreadPoolContext
import org.omg.CORBA.BAD_CONTEXT
import org.zeromq.ZStar
import kotlin.reflect.typeOf

//import furhatos.*

/*
This tab contains all the interaction steps
All functions are in the user.kt tab

Desired interaction flow:
Start
Give compliment
React to response

Responses: Accept compl, reject compl, return compl, other -> cancel
*/

var lastCompliment = ""
//Begin with a compliment; select topic random out of a certain list
val Start: State = state(Interaction){
    init{
        readFileVar("variablesR.txt") //only needs to be run once
        furhat.gesture(Gestures.Oh(duration = 2.0))
        furhat.say {
            random {
                +"Well, hello there!"
                +"What a sight!"
            }
            random {
                +"It's always lovely to meet someone so "
                +"So "
            }
            random {
                +"beautiful!"
                +"gorgeous!"
                +"handsome!"
            }
        }
        furhat.gesture(Gestures.Wink)

    }
    onEntry {
        createCompl() //Create a valid compliment
        furhat.say(compSent)
        furhat.gesture(Gestures.Nod(duration = 0.5))
        lastCompliment = compSent
        //reset() //reset the variables used to create a compliment
        furhat.listen()
    }

    onReentry {
        furhat.gesture(Gestures.BrowFrown)
        if(reentryCount > 2){
            furhat.say("I'm sorry to have made you uncomfortable.")
            furhat.say("I will just shut down now...")
            goto(BadPlace)
        }
        furhat.say("Please respond. Your approval means a lot to me")
        //createCompl()
        furhat.say{random{
            +"What I said was"
            +"I said"
            +"Allow me to reiterate"}}
        furhat.say(lastCompliment)
        reset()
        furhat.listen()
    }
    onResponse<Thanks>{
        furhat.gesture(Gestures.Nod)
        gazeVal++

        furhat.gesture(Gestures.Wink)
        furhat.say{random{
            +"The pleasure is all mine!"
            +"Just happy to make you happy"
            +"Only the best compliments for the best people"
            +"I am glad you like it"
        }}
        furhat.gesture(Gestures.BigSmile)
        if(gazeVal > happyThres){
            goto(HappyPlace)
        }
        goto(NewCompliment)
    }

    onPartialResponse<Thanks> {
        gazeVal++

        furhat.say{random{
            +"The pleasure is all mine!"
            +"Just happy to make you happy"
            +"Only the best compliments for the best people"
            +"I am glad you like it"
        }}

        raise(it, it.secondaryIntent)
    }

    onResponse<Yes>{
        gazeVal++

        furhat.say{random{
            +"Glad you agree with me"
            +"Gorgeous is as gorgeous does"
            +"Only the best compliments for the best people"
            +"I am glad you like it"
        }}
        if(gazeVal > happyThres){
            goto(HappyPlace)
        }
        goto(NewCompliment)
    }

    onResponse<No>{
        gazeVal--

        //println(gazeVal)
        furhat.gesture(Gestures.GazeAway)
        var singular = otherAttribute(lastTopic)
        var verbSay = ""
        if(singular == 1){
            verbSay = "is"
        } else{
            verbSay = "are"
        }
        furhat.say{random{
            +"You are too pretty to be so insecure"
            +"How can you say that?"
            +"I mean it"
            +"That doesn't change my opinion on your beautiful $lastTopic"
            +"It is! Why would I lie about your $lastTopic?"
            +"I never make jokes about your $lastTopic"
            +"You can't seriously believe your $lastTopic $verbSay not beautiful"
        }}
        if(gazeVal <= sadThres){
            goto(BadPlace)
        }
        goto(NewCompliment)
    }

    onResponse<Compliments>{
        gazeVal++

        furhat.gesture(Gestures.Oh)
        furhat.say{random{
            +"Oh, you silly"
            +"You are making me blush"
            +"That means so much coming from you"
            +"You just made my day"
            +"Can we continue talking about you?"
            +"Might be, but"
        }}
        if(gazeVal > happyThres){
            goto(HappyPlace)
        }
        goto(NewCompliment)
    }

    onPartialResponse<No> {
        gazeVal--

        furhat.say{random{
            +"Oh, sorry"
            +"My apologies "
            +"Nevertheless"
        }}
            raise(it, it.secondaryIntent)
        }

    onResponse<AttResponseList>{
        println(it.intent)
        var attributeSay: String = it.intent.toString()
        var singular = otherAttribute(attributeSay)
        var articleSay = ""
        if(singular == 1){
            articleSay = "a"
        } else{
            articleSay = ""
        }
        furhat.say {
            random {
                +"I just think that $articleSay $attributeSay would suit you very well"
                +"I always imagined $articleSay $attributeSay looks beautiful on you"
                +"You're the only one who would rock $articleSay $attributeSay like that"
                +"If I'd Google for $articleSay $attributeSay, you should be the top result"
            }
        }
        goto(NewCompliment)
    }

    onResponse("agree", "ok"){
        raise(Yes())
    }

    onResponse("you") {
        furhat.say {
            random {
                +"This isn't about me"
                +"I don't like talking about me"
                +"How about..."
                +"And we go back to you"
            }
        }
        goto(NewCompliment)
    }

    onResponse{
        furhat.gesture(Gestures.Thoughtful)
        furhat.say{random{
            +"I don't really know what to do with this"
            +"That's not really about you. Can we go back to talking about you please?"
            +"But you should hear this first"
        }}
        goto(NewCompliment)
    }

    onNoResponse {
        reentry()
    }
}

//Enter a new interaction state on a certain response (Helps with dealing with flow)
val NewCompliment : State = state(Interaction){
    onEntry {
        reset()
        createCompl()
        furhat.say(compSent)
        furhat.gesture(Gestures.Nod(duration = 0.5))
        lastCompliment = compSent
        //reset() //reset the variables used to create a compliment
        furhat.listen()
    }

    onReentry {
        if(reentryCount > 2){
            furhat.say("I'm sorry to have made you uncomfortable.")
            furhat.say("I will just shut down now...")
            goto(BadPlace)
        }
        furhat.say("Please respond. Your approval means a lot to me")
        //createCompl()
        furhat.say{random{
            +"What I said was"
            +"I said"
            +"Allow me to reiterate"}}
        furhat.say(lastCompliment)
        reset()
        furhat.listen()
    }

    onResponse<Yes>{
        gazeVal++

        furhat.gesture(Gestures.Nod, async = true)
        furhat.gesture(Gestures.BigSmile)
        furhat.say{random{
            +"Glad you agree with me"
            +"Gorgeous is as gorgeous does"
            +"Only the best compliments for the best people"
            +"I am glad you like it"
        }}
        if(gazeVal > happyThres){
            goto(HappyPlace)
        }
        goto(Start)
    }

    onResponse<No>{
        gazeVal--
        furhat.gesture(Gestures.GazeAway(duration =1.0))
        var singular = otherAttribute(lastTopic)
        var verbSay = ""
        if(singular == 1){
            verbSay = "is"
        } else{
            verbSay = "are"
        }
        furhat.say{random{
            +"You are too pretty to be so insecure"
            +"How can you say that?"
            +"I mean it"
            +"That doesn't change my opinion on your beautiful $lastTopic"
            +"It is! Why would I lie about your $lastTopic?"
            +"I never make jokes about your $lastTopic"
            +"You can't seriously believe your $lastTopic $verbSay not beautiful"
        }}
        if(gazeVal <= sadThres){
            goto(BadPlace)
        }
        goto(Start)
    }

    onPartialResponse<Thanks> {
        gazeVal++

        furhat.say{random{
            +"The pleasure is all mine!"
            +"Just happy to make you happy"
            +"Only the best compliments for the best people"
            +"I am glad you like it"
        }}

        raise(it, it.secondaryIntent)
    }

    onPartialResponse<No> {
        gazeVal--

        furhat.say{random{
            +"Oh, sorry"
            +"My apologies "
            +"Nevertheless"
        }}
        raise(it, it.secondaryIntent)
    }

    onResponse<Compliments>{
        gazeVal++
        furhat.gesture(Gestures.Oh(duration = 2.0))
        furhat.say{random{
            +"Oh, you silly"
            +"You are making me blush"
            +"That means so much coming from you"
            +"You just made my day"
            +"Can we continue talking about you?"
            +"Might be, but"
        }}
        if(gazeVal > happyThres){
            goto(HappyPlace)
        }
        goto(Start)
    }
    onResponse("agree", "ok"){
        raise(Yes())
    }

    onResponse<Thanks>{
        gazeVal++

        furhat.gesture(Gestures.Wink)
        furhat.say{random{
            +"The pleasure is all mine!"
            +"Just happy to make you happy"
            +"Only the best compliments for the best people"
            +"I am glad you like it"
        }}
        furhat.gesture(Gestures.BigSmile)
        if(gazeVal > happyThres){
            goto(HappyPlace)
        }
        goto(Start)
    }

    onResponse("you") {
        furhat.gesture(Gestures.ExpressAnger)
        furhat.say("It was never about me.")
        furhat.say("If you do not want to talk about yourself, you can leave")
        goto(Leave)
    }

    onResponse<AttResponseList>{
        println(it.intent)
        var attributeSay: String = it.intent.toString()
        var singular = otherAttribute(attributeSay)
        var articleSay = ""
        if(singular == 1){
            articleSay = "a"
        } else{
            articleSay = ""
        }
        furhat.say {
            random {
                +"I just think that $articleSay $attributeSay would suit you very well"
                +"I always imagined $articleSay $attributeSay looks beautiful on you"
                +"You're the only one who would rock $articleSay $attributeSay like that"
                +"If I'd Google for $articleSay $attributeSay, you should be the top result"
            }
        }
        goto(Start)
    }

    onNoResponse {
//        gazeVal--
//        if(gazeVal <= sadThres){
//            goto(BadPlace)
//        }
        reentry()
    }

    onResponse{
        furhat.say { random{
            +"Accept my appreciation for you!"
            +"How about this?"
            +"Notice me, senpai!"
        } }
        var sayingComps = 0
        while( sayingComps < 3){
            reset()
            createCompl()
            furhat.say(compSent)
            sayingComps++
        }
        goto(doubleOther)
    }
}

val NegativeResponse: State = state(Interaction){
    onEntry{
        furhat.gesture(Gestures.ExpressSad)
        furhat.say("No more negative feelings")
        furhat.say("You can return when you feel more positive again")
        goto(BadPlace)
    }
}

val doubleOther: State = state(Interaction){
    onEntry {
        furhat.ask{random {
            +"Happy now?"
            +"Satisfied?"
        }}
    }
    onReentry {
        if (reentryCount> 2){
            furhat.say("Let's try that again, shall we?")
            goto(Start)
        }
    }

    onResponse<Yes> {
        furhat.say{random {
            +"Wonderful. Now you can go on and be amazing"
            +"Keep being awesome, you shooting star"
            +"Just remember that you are amazing"
        }}
        goto(HappyPlace)
    }

    onResponse("agree", "ok"){
       raise(Yes())
    }

    onResponse<No> {
        gazeVal++
        if(gazeVal > happyThres){
            goto(HappyPlace)
        }
        furhat.say{random{
            +"Then I will just continue where I left off"
            +"Where was I?"}}
        goto(Start)
    }

    onResponse{
        furhat.say("Let's try that again, shall we?")
        goto(Start)
    }

    onNoResponse {
        furhat.say("Let's try that again")
        goto(Start)
    }
}

val Leave: State = state(Interaction){
    onEntry {
        furhat.listen()
    }

    onResponse<Yes>{
        raise(No())
    }

    onResponse<No> {
        furhat.gesture(Gestures.ExpressSad)
        furhat.say{
            +"See you around, "
            random{
                +"rock star."
                +"handsome."
                +"beautiful."
                +"gorgeous."
                +"cutie."
            }
        }
        goto(Idle)
    }

    onResponse {
        gazeVal++
        if(gazeVal > happyThres){
            goto(HappyPlace)
        }
        furhat.gesture(Gestures.BigSmile)
        furhat.say("That's more like it")
        goto(Start)
    }

    onNoResponse {
        raise(No())
    }
}

//Whenever the gaze meter is high: go to happy place
val HappyPlace: State = state(Happy){
    onEntry{
        gazeVal = 5
        furhat.say{random{
            +"Thank you for this wonderful conversation."
            +"This is the best conversation I have had in a long time!"
        }
        +"Please come back soon!"}
        goto(Idle)
    }
}

//Whenever the gaze meter is low: go to bad place
val BadPlace: State = state(Sad){
    gazeVal = 5
    onEntry{
        furhat.say{random{
            +"You are so mean..."
            +"That's not really nice of you"
            +"You make me so sad"
            +"I juwust wanted youwou to feel goowood"
        }}
        furhat.say("Bye then.")
        goto(Idle)
    }
}