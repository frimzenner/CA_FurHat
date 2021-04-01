package furhatos.app.project

import furhatos.app.project.flow.*
import furhatos.skills.Skill
import furhatos.flow.kotlin.*

class ProjectSkill : Skill() {
    override fun start() {
        Flow().run(Idle)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
