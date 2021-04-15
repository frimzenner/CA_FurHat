package furhatos.app.project.flow

import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.SimpleIntent
import furhatos.util.*

var gazeVal = 5 //variable ranging between 0 and 10 (ideally).
val happyThres = 8 //Variable when furhat becomes ultra happy
val sadThres = 2 //variable when furhat becomes ultra sad
// 3 failures allowed before going Sad
// 4 successes needed before going happy

val Idle: State = state {

    init {
        furhat.setVoice(Language.ENGLISH_AU, Gender.MALE, "Russell")

        if (users.count > 0) {
            furhat.attend(users.random)
            goto(Start)
        }
    }

    onEntry {
        furhat.attendNobody()
    }

    onUserEnter {
        furhat.attend(it)
        goto(Start)
    }

    onUserLeave{
        //gazeVal = 5
        furhat.attend(it)
        furhat.say {
            random {
                +"Good bye, "
                +"Hope to see you soon, "
                +"Can't wait to see you again, "
                +"Come back soon, "
            }
                random {
                    +"handsome."
                    +"beautiful."
                    +"gorgeous."
                    +"cutie."
                }
            }
        furhat.attendNobody()
        }
    }


val Interaction: State = state {
    onUserLeave(instant = true) {
        if (users.count > 0) {
            if (it == users.current) {
                furhat.attend(users.other)
                goto(Start)
            } else {
                furhat.glance(it)
            }
        } else {
            goto(Idle)
        }
        //gazeVal = 5
        furhat.say {
            random {
                +"Good bye, "
                +"Hope to see you soon, "
                +"Can't wait to see you again, "
                +"Come back soon, "
            }
            random {
                +"handsome."
                +"beautiful."
                +"gorgeous."
                +"cutie."
            }
        }
    }

    onUserEnter(instant = true) {
        furhat.glance(it)
    }
}

val Happy: State = state{
    onEntry {
        furhat.gesture(Gestures.Roll, async = true)
        furhat.gesture(Gestures.BigSmile)
    }
}

val Sad: State = state{
    onEntry {
        furhat.attendNobody()
        furhat.gesture(Gestures.GazeAway, async = true)
        furhat.gesture(Gestures.ExpressFear)
    }
}