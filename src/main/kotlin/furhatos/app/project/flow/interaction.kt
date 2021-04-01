package furhatos.app.project.flow

import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.app.project.nlu.*

val Start : State = state(Interaction) {

    onEntry {
        furhat.ask("Hi there. Do you like robots?")
    }

    onResponse<Yes>{
        furhat.say("Ta, I don't like humans, though.")
    }

    onResponse<No>{
        furhat.say("Ah, bummer mate. I do like humans, though")
    }
}
