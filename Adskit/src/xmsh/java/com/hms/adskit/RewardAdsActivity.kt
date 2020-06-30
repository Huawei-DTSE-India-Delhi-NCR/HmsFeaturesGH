package com.hms.adskit

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.hms.availabletoalllbraries.BaseActivity
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.reward.Reward
import com.huawei.hms.ads.reward.RewardAd
import com.huawei.hms.ads.reward.RewardAdLoadListener
import com.huawei.hms.ads.reward.RewardAdStatusListener
import kotlinx.android.synthetic.xmsh.activity_reward.*
import java.util.*

class RewardAdsActivity : BaseActivity(true) {

    private val PLUS_SCORE = 1

    private val MINUS_SCORE = 5

    private val RANGE = 2

    private var rewardedAd: RewardAd? = null

    private var score = 1

    private val defaultScore = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_reward)


    }

    /**
     * Load a rewarded ad.
     */
    private fun loadRewardAd() {
        if (rewardedAd == null) {
            rewardedAd = RewardAd(this, getString(R.string.ad_id_reward))
        }
        val rewardAdLoadListener: RewardAdLoadListener = object : RewardAdLoadListener() {
            override fun onRewardAdFailedToLoad(errorCode: Int) {
                Toast.makeText(this@RewardAdsActivity,"onRewardAdFailedToLoad errorCode is :$errorCode",Toast.LENGTH_SHORT).show()
            }

            override fun onRewardedLoaded() {
                Toast.makeText(this@RewardAdsActivity, "onRewardedLoaded", Toast.LENGTH_SHORT).show()
            }
        }
        rewardedAd?.loadAd(AdParam.Builder().build(), rewardAdLoadListener)
    }

    /**
     * Display a rewarded ad.
     */
    private fun rewardAdShow() {
        if (rewardedAd!!.isLoaded) {
            rewardedAd!!.show(this@RewardAdsActivity, object : RewardAdStatusListener() {
                override fun onRewardAdClosed() {
                    loadRewardAd()
                }

                override fun onRewardAdFailedToShow(errorCode: Int) {
                    Toast
                        .makeText(
                            this@RewardAdsActivity,
                            "onRewardAdFailedToShow errorCode is :$errorCode",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }

                override fun onRewardAdOpened() {
                    Toast.makeText(this@RewardAdsActivity, "onRewardAdOpened", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onRewarded(reward: Reward) {
                    // You are advised to grant a reward immediately and at the same time, check whether the reward
                    // takes effect on the server. If no reward information is configured, grant a reward based on the
                    // actual scenario.
                    val addScore =
                        if (reward.amount == 0) defaultScore else reward.amount
                    Toast.makeText(
                        this@RewardAdsActivity,
                        "Watch video show finished , add $addScore scores",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    score += addScore
                    setScore(score)
                    loadRewardAd()
                }
            })
        }
    }

    /**
     * Set a score.
     *
     * @param score
     */
    private fun setScore(score: Int) {
        score_count_text!!.text = "Score:$score"
    }

    /**
     * Load the button for watching a rewarded ad.
     */
    private fun loadWatchButton() {
        show_video_button.setOnClickListener(View.OnClickListener { rewardAdShow() })
    }

    /**
     * Load the button for starting a game.
     */
    private fun loadPlayButton() {
        play_button.setOnClickListener(View.OnClickListener { play() })
    }

    private fun loadScoreView() {
        score_count_text.setText("Score:$score")
    }

    /**
     * Used to play a game.
     */
    private fun play() {
        // If the score is 0, a message is displayed, asking users to watch the ad in exchange for scores.
        if (score == 0) {
            Toast.makeText(this, "Watch video ad to add score", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // The value 0 or 1 is returned randomly. If the value is 1, the score increases by 1. If the value is 0, the
        // score decreases by 5. If the score is a negative number, the score is set to 0.
        val random = Random().nextInt(RANGE)
        if (random == 1) {
            score += PLUS_SCORE
            Toast.makeText(this, "You win！", Toast.LENGTH_SHORT).show()
        } else {
            score -=MINUS_SCORE
            score = if (score < 0) 0 else score
            Toast.makeText(this, "You lose！", Toast.LENGTH_SHORT).show()
        }
        setScore(score)
    }
}