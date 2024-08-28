package com.chris.nakamai.common

import com.chris.nakamai.R

data class OnboardingComponent (
    val image: Int,
    val text: String
)

val listOnboardingComponent = listOf(
    OnboardingComponent(
        image = R.drawable.nakamai,
        text = "Welcome to Nakamai, your AI-powered friend, " +
                "always ready to chat and assist you."
    ),
    OnboardingComponent(
        image = R.drawable.onboard_001,
        text = "Explore smart conversations, get instant " +
                "answers, and connect like never before."
    ),
    OnboardingComponent(
        image = R.drawable.onboard_002,
        text = "Ready to meet your new friend? " +
                "Let’s get started!"
    ),
    OnboardingComponent(
        image = R.drawable.onboard_002,
        text = "To unlock Nakamai’s full potential, " +
                "we need access to your camera and gallery. " +
                "This allows you to share images and interact with our multimodal AI. " +
                "Your privacy is our top priority."
    )
)

