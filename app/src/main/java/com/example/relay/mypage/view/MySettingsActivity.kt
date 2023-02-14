package com.example.relay.mypage.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.R
import com.example.relay.databinding.ActivityMySettingsBinding
import com.example.relay.fcm.FireBaseClientService
import com.example.relay.fcm.FireBaseService
import com.example.relay.fcm.FirebaseInterface
import com.example.relay.fcm.data.UserDeviceTokenRes
import com.example.relay.login.view.LoginMainActivity
import com.example.relay.mypage.service.MySettingInterface
import com.example.relay.mypage.service.MySettingService
import com.example.relay.ui.MainActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_my_settings.view.*
import kotlinx.android.synthetic.main.dialog_change_img.view.*
import kotlinx.android.synthetic.main.dialog_change_msg.view.*
import kotlinx.android.synthetic.main.dialog_change_pw.view.*

class MySettingsActivity : AppCompatActivity(), MySettingInterface, FirebaseInterface {
    private val viewBinding: ActivityMySettingsBinding by lazy{
        ActivityMySettingsBinding.inflate(layoutInflater)
    }

    private var userIdx = prefs.getLong("userIdx", 0L)
    private var imgUrl = ""
    private var name = ""
    private var email = ""
    private var statusMsg = ""
    private var isAlarmOn = ""

    private val bucketURL = "https://team23-bucket.s3.ap-northeast-2.amazonaws.com/public/profile"
    private val imgList = arrayListOf(
        bucketURL + "/1.png",
        bucketURL + "/2.png",
        bucketURL + "/3.png",
        bucketURL + "/4.png",
        bucketURL + "/5.png"
    )

    private var msg = ""
    private var msgCheck = false
    private var img = ""
    private var imgCheck = false
    private var alarmCheck = false

    @SuppressLint("CommitPrefEdits", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        var bundle = intent.extras
        if (bundle != null) {
            imgUrl = bundle.getString("imgUrl", "")
            img = imgUrl
            name = bundle.getString("name", "")
            email = bundle.getString("email", "")
            statusMsg = bundle.getString("statusMsg", "")
            isAlarmOn = bundle.getString("isAlarmOn", "")
        }

        Glide.with(viewBinding.imgUser.context)
            .load(imgUrl)
            .into(viewBinding.imgUser)
        viewBinding.tvUserName.text = name
        viewBinding.tvEmail.text = email
        if (statusMsg != "") {
            viewBinding.tvInfo.text = statusMsg
        } else {
            viewBinding.tvInfo.text = "자신을 소개하는 문장을 적어보아요."
        }
        viewBinding.swAlarm.isChecked = isAlarmOn == "y"


        viewBinding.btnBack.setOnClickListener {
            if ((msgCheck) || (imgCheck) || (alarmCheck)) { // 상태메시지 or 이미지 변경
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("check", true)
                startActivity(intent)
            }
            finish()
        }

        viewBinding.btnLogout.setOnClickListener {
            // 저장된 계정 내용 초기화
//            prefs.edit().clear().apply()
//            val intent = Intent(this, LoginMainActivity::class.java)
//            finishAffinity()        // 스택에 쌓인 액티비티 비우기
//            startActivity(intent)
            FirebaseMessaging.getInstance().token.addOnSuccessListener {
                FireBaseClientService(this).tryDeleteUserDevice(it);
            }
        }

        // 비밀번호 변경하기
        viewBinding.btnChangePwd.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_change_pw, null)
            val alertDialog = AlertDialog.Builder(this).create()

            alertDialog?.setView(dialogView)
            alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog?.show()

            dialogView.btn_pw_cancel.setOnClickListener {
                alertDialog?.dismiss()
            }

            dialogView.btn_pw_save.setOnClickListener {
                val pw = prefs.getString("pw", "")
                val beforePw = dialogView.et_before_pw.text.toString()
                val newPw = dialogView.et_new_pw.text.toString()
                val checkPw = dialogView.et_check_pw.text.toString()

                if (beforePw != pw) {
                    Toast.makeText(this, "기존 비밀번호를 확인해주세요!", Toast.LENGTH_SHORT).show()
                } else {
                    if (newPw != checkPw) {
                        Toast.makeText(this, "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    } else { // 비밀번호 변경 값 전송
                        MySettingService(this).tryPatchUserPwd(newPw, checkPw)
                        alertDialog?.dismiss()
                    }
                }
            }
        }

        // 자기소개 변경하기
        viewBinding.btnChangeInfo.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_change_msg, null)
            val alertDialog = AlertDialog.Builder(this).create()

            alertDialog?.setView(dialogView)
            alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog?.show()

            dialogView.et_msg.setText(statusMsg) // 현재 상태메시지
            dialogView.et_msg.setSelection(dialogView.et_msg.length()) // 커서 끝에 설정

            dialogView.btn_msg_cancel.setOnClickListener {
                alertDialog?.dismiss()
            }

            dialogView.btn_msg_save.setOnClickListener {
                msg = dialogView.et_msg.text.toString()
                MySettingService(this).tryPatchUserMsg(msg)
                alertDialog?.dismiss()
            }
        }

        // 프로필 사진 변경
        viewBinding.btnEditImg.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_change_img, null)
            val alertDialog = AlertDialog.Builder(this).create()

            alertDialog?.setView(dialogView)
            alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog?.show()

            Glide.with(this)
                .load(imgUrl)
                .into(dialogView.findViewById(R.id.change_img))

            dialogView.btn_left.setOnClickListener {
                img = if (img.contains(bucketURL)) {
                    Log.d("imgUrl", img)
                    val n = Integer.parseInt(img.substring(img.length-5, img.length-4))
                    if (n != 1) {
                        imgList[n-2]
                    } else {
                        imgList[4]
                    }
                } else {
                    imgList[4]
                }

                Glide.with(this)
                    .load(img)
                    .into(dialogView.findViewById(R.id.change_img))
            }

            dialogView.btn_right.setOnClickListener {
                img = if (img.contains(bucketURL)) {
                    val n = Integer.parseInt(img.substring(img.length-5, img.length-4))
                    if (n != 5) {
                        imgList[n]
                    } else {
                        imgList[0]
                    }
                } else {
                    imgList[0]
                }

                Glide.with(this)
                    .load(img)
                    .into(dialogView.findViewById(R.id.change_img))
            }

            dialogView.btn_img_cancel.setOnClickListener {
                alertDialog?.dismiss()
            }

            dialogView.btn_img_save.setOnClickListener {
                alertDialog?.dismiss()
                MySettingService(this).tryPatchUserImg(img)
            }
        }

        // 알림 허용 변경
        viewBinding.swAlarm.setOnCheckedChangeListener { compoundButton, b ->
            MySettingService(this).tryPatchUserAlarm(userIdx)
            isAlarmOn = if (b) "y"
            else "n"
        }

    }

    override fun onPatchUserMsgSuccess() {
        Toast.makeText(this, "자기소개 변경이 완료되었습니다", Toast.LENGTH_SHORT).show()
        viewBinding.tvInfo.text = msg
        msgCheck = true
    }

    override fun onPatchUserMsgFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onPatchUserPwdSuccess() {
        Toast.makeText(this, "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onPatchUserPwdFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onPatchUserImgSuccess() {
        Toast.makeText(this, "프로필 사진 변경이 완료되었습니다", Toast.LENGTH_SHORT).show()
        Glide.with(viewBinding.imgUser.context)
            .load(img)
            .into(viewBinding.imgUser)
        imgCheck = true
    }

    override fun onPatchUserImgFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onPatchUserAlarmSuccess() {
        if (isAlarmOn == "y")
            Toast.makeText(this, "알림이 허용되었습니다.", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "알림이 비허용되었습니다.", Toast.LENGTH_SHORT).show()
        alarmCheck = true
    }

    override fun onPatchUserAlarmFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onPostDeleteDeviceSuccess(response: UserDeviceTokenRes) {
        prefs.edit().clear().apply()
        val intent = Intent(this, LoginMainActivity::class.java)
        finishAffinity()        // 스택에 쌓인 액티비티 비우기
        startActivity(intent)
    }
}
