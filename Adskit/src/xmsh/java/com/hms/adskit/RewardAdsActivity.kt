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

    private val PLUS_points = 0

    private val MINUS_points = 5

    private val RANGE = 2

    private var rewardedAd: RewardAd? = null

    private var points = 0

    private val defaultpoints = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_reward)
        supportActionBar?.title="Reward Ads"

        loadRewardAd()
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
        loadWatchButton()
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

                    Toast.makeText(
                        this@RewardAdsActivity,
                        "Watch video show finished , add  pointss",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    points += 1
                    setpoints(points)
                    loadRewardAd()
                }
            })
        }
    }

    /**
     * Set a points.
     *
     * @param points
     */
    private fun setpoints(points: Int) {
        score_count_text!!.text = "points:$points"
    }

    /**
     * Load the button for watching a rewarded ad.
     */
    private fun loadWatchButton() {
        show_video_button.setOnClickListener(View.OnClickListener { rewardAdShow() })
    }



    /**
     * Used to play a game.
     */
    private fun play() {
        // If the points is 0, a message is displayed, asking users to watch the ad in exchange for pointss.
        if (points == 0) {
            Toast.makeText(this, "Watch video ad to add points", Toast.LENGTH_SHORT)
                .show()
            return
        }


        setpoints(points)
    }
}