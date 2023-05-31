package com.lamesa.socialdown.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.dialogs.BottomDialog
import com.kongzue.dialogx.dialogs.BottomMenu
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.PopNotification
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.impl.ActivityLifecycleImpl.getApplicationContext
import com.kongzue.dialogx.interfaces.OnBindView
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack
import com.kongzue.dialogx.util.TextInfo
import com.lamesa.socialdown.R
import com.lamesa.socialdown.app.SDApp.Context.tinyDB
import com.lamesa.socialdown.downloader.PermissionsUtil
import com.lamesa.socialdown.ui.view.main.MainActivity
import com.lamesa.socialdown.utils.Constansts.Analytics.HasRated
import com.lamesa.socialdown.utils.Constansts.Analytics.TBDownloads
import com.lamesa.socialdown.utils.SocialHelper.shareApp
import net.khirr.android.privacypolicy.PrivacyPolicyDialog

class DialogXUtils : DialogX() {

    object NotificationX {
        fun showError(message: String): PopNotification {
            SDAnalytics().eventError(message)
            return PopNotification.show(message).iconError().showLong()
        }

        fun showMessage(message: String): PopNotification {
            return PopNotification.show(message)
        }

        fun showWarning(message: String): PopNotification {
            return PopNotification.show(message).iconWarning()
        }

        fun showCustomMessage(title: String, msg: String): PopNotification {
            return PopNotification.build()
                .setTitle(title)
                .setMessage(msg)
                .setIconResId(R.drawable.alerter_ic_notifications)
                .showLong()
                .setButton("OK") { popNotification, v ->
                    SDAnalytics().eventViewMessage()
                    false
                }
        }

        fun showUpdate(): MessageDialog? {
            return MessageDialog.build()
                .setTitle(getApplicationContext().getString(R.string.text_updateApp))
                .setMessage(getApplicationContext().getString(R.string.msg_updateApp))
                .setOkButton(
                    getApplicationContext().getString(R.string.update)
                ) { baseDialog, v ->
                    SDAnalytics().eventViewUpdate()
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://rebrand.ly/upsocialdown"))
                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    getApplicationContext().startActivity(browserIntent)
                    false
                }
        }
    }

    object ToastX {
        fun showError(message: String): PopTip {
            SDAnalytics().eventError(message)
            return PopTip.show(message).iconError()
        }

        fun showSuccess(message: String): PopTip {
            return PopTip.show(message).iconSuccess()
        }

        fun showWarning(message: String): PopTip {
            return PopTip.show(message).iconWarning()
        }

        fun showMessage(message: String): PopTip {
            return PopTip.show(message)
        }

    }

    object LoadingX {

        private var isShowing = false
        private val dialogLoading = BottomDialog.build(object :
            OnBindView<BottomDialog?>(R.layout.dialog_loading) {
            override fun onBind(dialog: BottomDialog?, v: View?) {
                SDAd().initBannerAdLoading(v)
            }
        }).setCancelable(false)

        fun showLoading() {
            if (!isShowing) {
                dialogLoading.show()
                isShowing = true
            }
        }

        fun hideLoading() {
            if (isShowing) {
                dialogLoading.hide()
                isShowing = false
            }
        }

        fun setTitle(text: String) {
            dialogLoading.customView.findViewById<TextView>(R.id.tv_titleDialog).text = text
        }

        fun setDescription(text: String) {
            dialogLoading.customView.findViewById<TextView>(R.id.tv_descDialog).text = text
        }
    }

    object Policies {
        internal fun dialogPrivacyPolicies(context: Context) {
            val dialog = PrivacyPolicyDialog(
                context as AppCompatActivity,
                "https://lugumusic.page.link/terms",
                "https://lugumusic.page.link/privacy"
            )

            dialog.title = "Terms of Service & Privacy Policy"
            dialog.addPoliceLine("Terms and Conditions")
            dialog.addPoliceLine("The words of which the initial letter is capitalized have meanings defined under the following conditions. The following definitions shall have the same meaning regardless of whether they appear in singular or in plural.")
            dialog.addPoliceLine(
                "For the purposes of these Terms and Conditions:\n\n" +
                        "\n\n" +
                        "Affiliate means an entity that controls, is controlled by or is under common control with a party, where \"control\" means ownership of 50% or more of the shares, equity interest or other securities entitled to vote for election of directors or other managing authority.\n\n" +
                        "\n\n" +
                        "Country refers to: Colombia\n\n" +
                        "\n\n" +
                        "Company (referred to as either \"the Company\", \"We\", \"Us\" or \"Our\" in this Agreement) refers to SocialDown.\n\n" +
                        "\n\n" +
                        "Device means any device that can access the Service such as a computer, a cellphone or a digital tablet.\n\n" +
                        "\n\n" +
                        "Service refers to the Website.\n\n" +
                        "\n\n" +
                        "Terms and Conditions (also referred as \"Terms\") mean these Terms and Conditions that form the entire agreement between You and the Company regarding the use of the Service. This Terms and Conditions agreement has been created with the help of the Terms and Conditions Generator.\n\n" +
                        "\n\n" +
                        "Third-party Social Media Service means any services or content (including data, information, products or services) provided by a third-party that may be displayed, included or made available by the Service.\n\n" +
                        "\n\n" +
                        "Website refers to SocialDown, accessible from socialdownapp.com\n\n" +
                        "\n\n" +
                        "You means the individual accessing or using the Service, or the company, or other legal entity on behalf of which such individual is accessing or using the Service, as applicable."
            )
            dialog.addPoliceLine(
                ("Acknowledgment \n\n " + "These are the Terms and Conditions governing the use of this Service and the agreement that operates between You and the Company. These Terms and Conditions set out the rights and obligations of all users regarding the use of the Service.\n\n" +
                        "\n\n" +
                        "Your access to and use of the Service is conditioned on Your acceptance of and compliance with these Terms and Conditions. These Terms and Conditions apply to all visitors, users and others who access or use the Service.\n\n" +
                        "\n\n" +
                        "By accessing or using the Service You agree to be bound by these Terms and Conditions. If You disagree with any part of these Terms and Conditions then You may not access the Service.\n\n" +
                        "\n\n" +
                        "You represent that you are over the age of 18. The Company does not permit those under 18 to use the Service.\n\n" +
                        "\n\n" +
                        "Your access to and use of the Service is also conditioned on Your acceptance of and compliance with the Privacy Policy of the Company. Our Privacy Policy describes Our policies and procedures on the collection, use and disclosure of Your personal information when You use the Application or the Website and tells You about Your privacy rights and how the law protects You. Please read Our Privacy Policy carefully before using Our Service.")
            )


            dialog.addPoliceLine(
                ("com.lamesa.socialdown.domain.response.facebook.Links to Other Websites \n\n " + "Our Service may contain links to third-party web sites or services that are not owned or controlled by the Company.\n\n" +
                        "\n\n" +
                        "The Company has no control over, and assumes no responsibility for, the content, privacy policies, or practices of any third party web sites or services. You further acknowledge and agree that the Company shall not be responsible or liable, directly or indirectly, for any damage or loss caused or alleged to be caused by or in connection with the use of or reliance on any such content, goods or services available on or through any such web sites or services.\n\n" +
                        "\n\n" +
                        "We strongly advise You to read the terms and conditions and privacy policies of any third-party web sites or services that You visit.")
            )

            dialog.addPoliceLine(
                ("\"AS IS\" and \"AS AVAILABLE\" Disclaimer \n\n" + "The Service is provided to You \"AS IS\" and \"AS AVAILABLE\" and with all faults and defects without warranty of any kind. To the maximum extent permitted under applicable law, the Company, on its own behalf and on behalf of its Affiliates and its and their respective licensors and service providers, expressly disclaims all warranties, whether express, implied, statutory or otherwise, with respect to the Service, including all implied warranties of merchantability, fitness for a particular purpose, title and non-infringement, and warranties that may arise out of course of dealing, course of performance, usage or trade practice. Without limitation to the foregoing, the Company provides no warranty or undertaking, and makes no representation of any kind that the Service will meet Your requirements, achieve any intended results, be compatible or work with any other software, applications, systems or services, operate without interruption, meet any performance or reliability standards or be error free or that any errors or defects can or will be corrected.\n\n" +
                        "\n\n" +
                        "Without limiting the foregoing, neither the Company nor any of the company's provider makes any representation or warranty of any kind, express or implied: (i) as to the operation or availability of the Service, or the information, content, and materials or products included thereon; (ii) that the Service will be uninterrupted or error-free; (iii) as to the accuracy, reliability, or currency of any information or content provided through the Service; or (iv) that the Service, its servers, the content, or e-mails sent from or on behalf of the Company are free of viruses, scripts, trojan horses, worms, malware, timebombs or other harmful components.\n\n" +
                        "\n\n" +
                        "Some jurisdictions do not allow the exclusion of certain types of warranties or limitations on applicable statutory rights of a consumer, so some or all of the above exclusions and limitations may not apply to You. But in such a case the exclusions and limitations set forth in this section shall be applied to the greatest extent enforceable under applicable law.")
            )

            dialog.addPoliceLine(
                ("Limitation of Liability \n\n" + "Notwithstanding any damages that You might incur, the entire liability of the Company and any of its suppliers under any provision of this Terms and Your exclusive remedy for all of the foregoing shall be limited to the amount actually paid by You through the Service or 100 USD if You haven't purchased anything through the Service.\n\n" +
                        "\n\n" +
                        "To the maximum extent permitted by applicable law, in no event shall the Company or its suppliers be liable for any special, incidental, indirect, or consequential damages whatsoever (including, but not limited to, damages for loss of profits, loss of data or other information, for business interruption, for personal injury, loss of privacy arising out of or in any way related to the use of or inability to use the Service, third-party software and/or third-party hardware used with the Service, or otherwise in connection with any provision of this Terms), even if the Company or any supplier has been advised of the possibility of such damages and even if the remedy fails of its essential purpose.\n\n" +
                        "\n\n" +
                        "Some states do not allow the exclusion of implied warranties or limitation of liability for incidental or consequential damages, which means that some of the above limitations may not apply. In these states, each party's liability will be limited to the greatest extent permitted by law.")
            )
            dialog.addPoliceLine("Governing Law \n\n The laws of the Country, excluding its conflicts of law rules, shall govern this Terms and Your use of the Service. Your use of the Application may also be subject to other local, state, national, or international laws.")

            dialog.addPoliceLine("Disputes Resolution \n\n" + "If You have any concern or dispute about the Service, You agree to first try to resolve the dispute informally by contacting the Company.")


            dialog.addPoliceLine("For European Union (EU) \n\nUsers If You are a European Union consumer, you will benefit from any mandatory provisions of the law of the country in which you are resident in.")

            dialog.addPoliceLine("United States Legal Compliance \n\n" + "You represent and warrant that (i) You are not located in a country that is subject to the United States government embargo, or that has been designated by the United States government as a \"terrorist supporting\" country, and (ii) You are not listed on any United States government list of prohibited or restricted parties.")

            dialog.addPoliceLine(
                ("Severability and Waiver \n\nSeverability\n\n" +
                        "If any provision of these Terms is held to be unenforceable or invalid, such provision will be changed and interpreted to accomplish the objectives of such provision to the greatest extent possible under applicable law and the remaining provisions will continue in full force and effect.\n\n" +
                        "\n\n" +
                        "Waiver\n\n" +
                        "Except as provided herein, the failure to exercise a right or to require performance of an obligation under this Terms shall not effect a party's ability to exercise such right or require such performance at any time thereafter nor shall be the waiver of a breach constitute a waiver of any subsequent breach.")
            )

            dialog.addPoliceLine("Translation Interpretation \n\n" + "These Terms and Conditions may have been translated if We have made them available to You on our Service. You agree that the original English text shall prevail in the case of a dispute.")

            dialog.addPoliceLine(
                ("Changes to These Terms and Conditions \n\nWe reserve the right, at Our sole discretion, to modify or replace these Terms at any time. If a revision is material We will make reasonable efforts to provide at least 30 days' notice prior to any new terms taking effect. What constitutes a material change will be determined at Our sole discretion.\n\n" +
                        "\n\n" +
                        "By continuing to access or use Our Service after those revisions become effective, You agree to be bound by the revised terms. If You do not agree to the new terms, in whole or in part, please stop using the website and the Service.")
            )
            dialog.addPoliceLine(
                ("Contact Us \n\n" + "If you have any questions about these Terms and Conditions, You can contact us:\n\n" +
                        "\n\n" +
                        "By email: socialdownapp@gmail.com")
            )

            val sharedPref = context.getPreferences(Context.MODE_PRIVATE)
            dialog.onClickListener = object : PrivacyPolicyDialog.OnClickListener {
                override fun onAccept(isFirstTime: Boolean) {
                    Log.e("SplashActivity", "Policies accepted")
                    with(sharedPref.edit()) {
                        putBoolean("policies", true)
                        apply()
                    }
                    context.startActivity(Intent(context, MainActivity::class.java))
                }

                override fun onCancel() {
                    Log.e("MainActivity", "Policies not accepted")
                    with(sharedPref.edit()) {
                        putBoolean("politics", false)
                        apply()
                    }
                    (context as Activity).finish()
                }
            }

            if (!sharedPref.getBoolean("policies", false)) {
                dialog.show()
            } else {
                context.startActivity(Intent(context, MainActivity::class.java))
                context.finish()
            }

        }

        internal fun dialogPrivacyPolicies2(context: Context) {
            MessageDialog.show(
                "Terms of Service & Privacy Policy",
                "Privacy Policy for SocialDown\n\n" +
                        "\n\n" +
                        "Privacy Policy\n\n" +
                        "Last updated: December 28, 2022\n\n" +
                        "\n\n" +
                        "This Privacy Policy describes Our policies and procedures on the collection, use and disclosure of Your information when You use the Service and tells You about Your privacy rights and how the law protects You.\n\n" +
                        "\n\n" +
                        "We use Your Personal data to provide and improve the Service. By using the Service, You agree to the collection and use of information in accordance with this Privacy Policy. This Privacy Policy has been created with the help of the Privacy Policy Generator.\n\n" +
                        "\n\n" +
                        "Interpretation and Definitions\n\n" +
                        "Interpretation\n\n" +
                        "The words of which the initial letter is capitalized have meanings defined under the following conditions. The following definitions shall have the same meaning regardless of whether they appear in singular or in plural.\n\n" +
                        "\n\n" +
                        "Definitions\n\n" +
                        "For the purposes of this Privacy Policy:\n\n" +
                        "\n\n" +
                        "Account means a unique account created for You to access our Service or parts of our Service.\n\n" +
                        "\n\n" +
                        "Affiliate means an entity that controls, is controlled by or is under common control with a party, where \"control\" means ownership of 50% or more of the shares, equity interest or other securities entitled to vote for election of directors or other managing authority.\n\n" +
                        "\n\n" +
                        "Application refers to SocialDown, the software program provided by the Company.\n\n" +
                        "\n\n" +
                        "Company (referred to as either \"the Company\", \"We\", \"Us\" or \"Our\" in this Agreement) refers to SocialDown.\n\n" +
                        "\n\n" +
                        "Country refers to: Colombia\n\n" +
                        "\n\n" +
                        "Device means any device that can access the Service such as a computer, a cellphone or a digital tablet.\n\n" +
                        "\n\n" +
                        "Personal Data is any information that relates to an identified or identifiable individual.\n\n" +
                        "\n\n" +
                        "Service refers to the Application.\n\n" +
                        "\n\n" +
                        "Service Provider means any natural or legal person who processes the data on behalf of the Company. It refers to third-party companies or individuals employed by the Company to facilitate the Service, to provide the Service on behalf of the Company, to perform services related to the Service or to assist the Company in analyzing how the Service is used.\n\n" +
                        "\n\n" +
                        "Usage Data refers to data collected automatically, either generated by the use of the Service or from the Service infrastructure itself (for example, the duration of a page visit).\n\n" +
                        "\n\n" +
                        "You means the individual accessing or using the Service, or the company, or other legal entity on behalf of which such individual is accessing or using the Service, as applicable.\n\n" +
                        "\n\n" +
                        "Collecting and Using Your Personal Data\n\n" +
                        "Types of Data Collected\n\n" +
                        "Personal Data\n\n" +
                        "While using Our Service, We may ask You to provide Us with certain personally identifiable information that can be used to contact or identify You. Personally identifiable information may include, but is not limited to:\n\n" +
                        "\n\n" +
                        "First name and last name\n\n" +
                        "\n\n" +
                        "Usage Data\n\n" +
                        "\n\n" +
                        "Usage Data\n\n" +
                        "Usage Data is collected automatically when using the Service.\n\n" +
                        "\n\n" +
                        "Usage Data may include information such as Your Device's Internet Protocol address (e.g. IP address), browser type, browser version, the pages of our Service that You visit, the time and date of Your visit, the time spent on those pages, unique device identifiers and other diagnostic data.\n\n" +
                        "\n\n" +
                        "When You access the Service by or through a mobile device, We may collect certain information automatically, including, but not limited to, the type of mobile device You use, Your mobile device unique ID, the IP address of Your mobile device, Your mobile operating system, the type of mobile Internet browser You use, unique device identifiers and other diagnostic data.\n\n" +
                        "\n\n" +
                        "We may also collect information that Your browser sends whenever You visit our Service or when You access the Service by or through a mobile device.\n\n" +
                        "\n\n" +
                        "Use of Your Personal Data\n\n" +
                        "The Company may use Personal Data for the following purposes:\n\n" +
                        "\n\n" +
                        "To provide and maintain our Service, including to monitor the usage of our Service.\n\n" +
                        "\n\n" +
                        "To manage Your Account: to manage Your registration as a user of the Service. The Personal Data You provide can give You access to different functionalities of the Service that are available to You as a registered user.\n\n" +
                        "\n\n" +
                        "For the performance of a contract: the development, compliance and undertaking of the purchase contract for the products, items or services You have purchased or of any other contract with Us through the Service.\n\n" +
                        "\n\n" +
                        "To contact You: To contact You by email, telephone calls, SMS, or other equivalent forms of electronic communication, such as a mobile application's push notifications regarding updates or informative communications related to the functionalities, products or contracted services, including the security updates, when necessary or reasonable for their implementation.\n\n" +
                        "\n\n" +
                        "To provide You with news, special offers and general information about other goods, services and events which we offer that are similar to those that you have already purchased or enquired about unless You have opted not to receive such information.\n\n" +
                        "\n\n" +
                        "To manage Your requests: To attend and manage Your requests to Us.\n\n" +
                        "\n\n" +
                        "For business transfers: We may use Your information to evaluate or conduct a merger, divestiture, restructuring, reorganization, dissolution, or other sale or transfer of some or all of Our assets, whether as a going concern or as part of bankruptcy, liquidation, or similar proceeding, in which Personal Data held by Us about our Service users is among the assets transferred.\n\n" +
                        "\n\n" +
                        "For other purposes: We may use Your information for other purposes, such as data analysis, identifying usage trends, determining the effectiveness of our promotional campaigns and to evaluate and improve our Service, products, services, marketing and your experience.\n\n" +
                        "\n\n" +
                        "We may share Your personal information in the following situations:\n\n" +
                        "\n\n" +
                        "With Service Providers: We may share Your personal information with Service Providers to monitor and analyze the use of our Service, to contact You.\n\n" +
                        "For business transfers: We may share or transfer Your personal information in connection with, or during negotiations of, any merger, sale of Company assets, financing, or acquisition of all or a portion of Our business to another company.\n\n" +
                        "With Affiliates: We may share Your information with Our affiliates, in which case we will require those affiliates to honor this Privacy Policy. Affiliates include Our parent company and any other subsidiaries, joint venture partners or other companies that We control or that are under common control with Us.\n\n" +
                        "With business partners: We may share Your information with Our business partners to offer You certain products, services or promotions.\n\n" +
                        "With other users: when You share personal information or otherwise interact in the public areas with other users, such information may be viewed by all users and may be publicly distributed outside.\n\n" +
                        "With Your consent: We may disclose Your personal information for any other purpose with Your consent.\n\n" +
                        "Retention of Your Personal Data\n\n" +
                        "The Company will retain Your Personal Data only for as long as is necessary for the purposes set out in this Privacy Policy. We will retain and use Your Personal Data to the extent necessary to comply with our legal obligations (for example, if we are required to retain your data to comply with applicable laws), resolve disputes, and enforce our legal agreements and policies.\n\n" +
                        "\n\n" +
                        "The Company will also retain Usage Data for internal analysis purposes. Usage Data is generally retained for a shorter period of time, except when this data is used to strengthen the security or to improve the functionality of Our Service, or We are legally obligated to retain this data for longer time periods.\n\n" +
                        "\n\n" +
                        "Transfer of Your Personal Data\n\n" +
                        "Your information, including Personal Data, is processed at the Company's operating offices and in any other places where the parties involved in the processing are located. It means that this information may be transferred to — and maintained on — computers located outside of Your state, province, country or other governmental jurisdiction where the data protection laws may differ than those from Your jurisdiction.\n\n" +
                        "\n\n" +
                        "Your consent to this Privacy Policy followed by Your submission of such information represents Your agreement to that transfer.\n\n" +
                        "\n\n" +
                        "The Company will take all steps reasonably necessary to ensure that Your data is treated securely and in accordance with this Privacy Policy and no transfer of Your Personal Data will take place to an organization or a country unless there are adequate controls in place including the security of Your data and other personal information.\n\n" +
                        "\n\n" +
                        "Delete Your Personal Data\n\n" +
                        "You have the right to delete or request that We assist in deleting the Personal Data that We have collected about You.\n\n" +
                        "\n\n" +
                        "Our Service may give You the ability to delete certain information about You from within the Service.\n\n" +
                        "\n\n" +
                        "You may update, amend, or delete Your information at any time by signing in to Your Account, if you have one, and visiting the account settings section that allows you to manage Your personal information. You may also contact Us to request access to, correct, or delete any personal information that You have provided to Us.\n\n" +
                        "\n\n" +
                        "Please note, however, that We may need to retain certain information when we have a legal obligation or lawful basis to do so.\n\n" +
                        "\n\n" +
                        "Disclosure of Your Personal Data\n\n" +
                        "Business Transactions\n\n" +
                        "If the Company is involved in a merger, acquisition or asset sale, Your Personal Data may be transferred. We will provide notice before Your Personal Data is transferred and becomes subject to a different Privacy Policy.\n\n" +
                        "\n\n" +
                        "Law enforcement\n\n" +
                        "Under certain circumstances, the Company may be required to disclose Your Personal Data if required to do so by law or in response to valid requests by public authorities (e.g. a court or a government agency).\n\n" +
                        "\n\n" +
                        "Other legal requirements\n\n" +
                        "The Company may disclose Your Personal Data in the good faith belief that such action is necessary to:\n\n" +
                        "\n\n" +
                        "Comply with a legal obligation\n\n" +
                        "Protect and defend the rights or property of the Company\n\n" +
                        "Prevent or investigate possible wrongdoing in connection with the Service\n\n" +
                        "Protect the personal safety of Users of the Service or the public\n\n" +
                        "Protect against legal liability\n\n" +
                        "Security of Your Personal Data\n\n" +
                        "The security of Your Personal Data is important to Us, but remember that no method of transmission over the Internet, or method of electronic storage is 100% secure. While We strive to use commercially acceptable means to protect Your Personal Data, We cannot guarantee its absolute security.\n\n" +
                        "\n\n" +
                        "Children's Privacy\n\n" +
                        "Our Service does not address anyone under the age of 13. We do not knowingly collect personally identifiable information from anyone under the age of 13. If You are a parent or guardian and You are aware that Your child has provided Us with Personal Data, please contact Us. If We become aware that We have collected Personal Data from anyone under the age of 13 without verification of parental consent, We take steps to remove that information from Our servers.\n\n" +
                        "\n\n" +
                        "If We need to rely on consent as a legal basis for processing Your information and Your country requires consent from a parent, We may require Your parent's consent before We collect and use that information.\n\n" +
                        "\n\n" +
                        "com.lamesa.socialdown.domain.response.facebook.Links to Other Websites\n\n" +
                        "Our Service may contain links to other websites that are not operated by Us. If You click on a third party link, You will be directed to that third party's site. We strongly advise You to review the Privacy Policy of every site You visit.\n\n" +
                        "\n\n" +
                        "We have no control over and assume no responsibility for the content, privacy policies or practices of any third party sites or services.\n\n" +
                        "\n\n" +
                        "Changes to this Privacy Policy\n\n" +
                        "We may update Our Privacy Policy from time to time. We will notify You of any changes by posting the new Privacy Policy on this page.\n\n" +
                        "\n\n" +
                        "We will let You know via email and/or a prominent notice on Our Service, prior to the change becoming effective and update the \"Last updated\" date at the top of this Privacy Policy.\n\n" +
                        "\n\n" +
                        "You are advised to review this Privacy Policy periodically for any changes. Changes to this Privacy Policy are effective when they are posted on this page.\n\n" +
                        "\n\n" +
                        "Contact Us\n\n" +
                        "If you have any questions about this Privacy Policy, You can contact us:\n\n" +
                        "\n\n" +
                        "By email: socialdownapp@gmail.com\n\n".trimIndent(), "OK", "CLOSE"
            )

            SDAd().showInterOrVideo(context)

        }

        internal fun dialogDCMA(context: Context) {
            MessageDialog.show(
                "DMCA",
                """
                            No multimedia file is being hosted by us on this app.
                            
                            We are not associated with the list of contents found on remote servers. We have no connection or association with such content.
                            The mp3, jpg, png files that are available are not hosted on SocialDown app and are hosted on other servers (therefore, not our host service).
                            This app SocialDown functions as a lofi music search engine and does not store or host any files or other copyrighted material. We follow copyright laws, but if you find any search results that you feel are illegal, you are asked to complete the form and send an email to lugulofimusic@gmail.com
                            In fact, we adhere to the rights of producers and artists. We assure you that your work will be safe and legal, which will result in a positive experience for each of you, whether you are a creator or a musical artist. Please note that if any person knowingly or intentionally misrepresents any material or activity listed in Section 512(f), it would be considered a violation of copyright law. Then, if you are doing so, you are liable for your own harm. But keep one thing in mind: Don’t make any false claims about the infringed content!
                            
                            The complete information contained in the legal notice may also be sent to the interested party providing the content that is being infringed.
                            """.trimIndent(), "OK", "CLOSE"
            ).okButton = "OK"

            SDAd().showInterOrVideo(context)
        }

        internal fun dialogPermissions(context: Context) {
            if (!PermissionsUtil.checkWriteStoragePermission(context as Activity)) {
                BottomDialog.show(
                    context.getString(R.string.title_permissions),
                    context.getString(R.string.msg_permissions)
                )
                    .setCancelButton(
                        "OK"
                    ) { baseDialog, v ->
                        PermissionsUtil.requestWriteStoragePermission(context)
                        false
                    }.isCancelable = false
            }
        }

        internal fun dialogStars(context: Context) {
            if (!tinyDB.getBoolean(HasRated) && tinyDB.getInt(TBDownloads) >= 3) {
                val reviewManager = ReviewManagerFactory.create(context)
                val request = reviewManager.requestReviewFlow()
                request.addOnCompleteListener { task: Task<ReviewInfo> ->
                    if (task.isSuccessful) {
                        // continue with flow
                        SDAnalytics().eventViewStars()
                        val reviewInfo = task.result
                        val reviewFlow =
                            reviewManager.launchReviewFlow(context as Activity, reviewInfo)
                        reviewFlow.addOnCompleteListener { task: Task<Void> ->
                            if (task.isSuccessful) {
                                // review launched successfully
                                tinyDB.putBoolean(HasRated, true)
                            } else {
                                // review launch failed
                                tinyDB.putBoolean(HasRated, false)
                                if (tinyDB.getInt(TBDownloads) >= 4) {
                                    tinyDB.putBoolean(HasRated, true)
                                }
                            }
                        }

                    } else {
                        // review flow not available
                        tinyDB.putBoolean(HasRated, false)
                    }
                }
            } else {
                when (tinyDB.getInt(TBDownloads)) {
                    10 -> tinyDB.putBoolean(HasRated, false)
                    20 -> tinyDB.putBoolean(HasRated, false)
                    30 -> tinyDB.putBoolean(HasRated, false)
                    40 -> tinyDB.putBoolean(HasRated, false)
                }
            }
        }
    }

    internal fun dialogMainMenu(context: Context) {
        BottomMenu.show(
            arrayOf(
                "How Use?",
                context.getString(R.string.text_share),
                context.getString(R.string.text_Feedback),
                context.getString(R.string.text_ReportBurg),
                context.getString(R.string.text_ProvacyPolicy), "DCMA"
            )
        )
            .setTitle("MENU")
            .setOnIconChangeCallBack(object : OnIconChangeCallBack<BottomMenu?>(true) {
                override fun getIcon(dialog: BottomMenu?, index: Int, menuText: String?): Int {
                    when (menuText) {
                        "How Use?" -> return R.drawable.ic_howuse
                        context.getString(R.string.text_share) -> return R.drawable.ic_send
                        context.getString(R.string.text_Feedback) -> return R.drawable.ic_feedback
                        context.getString(R.string.text_ReportBurg) -> return R.drawable.ic_bug
                        context.getString(R.string.text_ProvacyPolicy) -> return R.drawable.ic_information
                        "DCMA" -> return R.drawable.ic_link
                    }
                    dialog!!.hide()
                    return 0
                }
            }).setOnMenuItemClickListener { dialog, text, index ->
                when (text) {
                    "How Use?" -> dialogHowUse(context)
                    context.getString(R.string.text_share) -> shareApp(context as Activity)
                    context.getString(R.string.text_Feedback) -> context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://rebrand.ly/sd-feedback")
                        )
                    )
                    context.getString(R.string.text_ReportBurg) -> context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://rebrand.ly/sd-bugreporting")
                        )
                    )
                    context.getString(R.string.text_ProvacyPolicy) -> Policies.dialogPrivacyPolicies2(
                        context
                    )
                    "DCMA" -> Policies.dialogDCMA(context)
                }
                dialog!!.hide()
                true
            }
    }

    private fun dialogHowUse(context: Context) {
        MessageDialog.show(
            "Welcome to the SocialDown tutorial!",
            "SocialDown is a free app that allows you to download videos from Facebook, Instagram, and TikTok by simply pasting the video link. Here's how to use it:\n\nFind the video you want to download on Facebook, Instagram, or TikTok and copy the link.\n\n1. Open SocialDown and paste the link into the input field.\n\n2. Tap the 'Download' button to begin the download process.\n\n3. Once the download is complete, you can find the video in your device's storage.\n\n4. That's it! You can now download any video from Facebook, Instagram, or TikTok using SocialDown.\n\nHappy downloading!",
            "OK",
            "CLOSE"
        ).okButton = "OK"
        SDAd().showInterOrVideo(context)
    }

    internal fun dialogLimitDownloads(context: Context) {
        MessageDialog.show(
            context.getString(R.string.dialog_title_limit_reached),
            context.getString(R.string.dialog_message_limit_reached),
            context.getString(R.string.dialog_button_ok),
            context.getString(R.string.dialog_button_close)
        ).okButton = context.getString(R.string.dialog_button_ok)
    }


    internal fun dialogCongratulations(context: Context) {
        val textInfo = TextInfo()
        textInfo.gravity = Gravity.CENTER
        for (i in 10..100 step 30) {
            if (tinyDB.getInt(TBDownloads) == i) {
                MessageDialog.show(
                    i.toString() + " " + context.getString(R.string.text_Downloads) + "! " + "\uD83E\uDD73 \uD83C\uDF89",
                    context.getString(R.string.msd_congratulations),
                    "Rate Now \uD83C\uDF1F",
                    "No Thanks \uD83D\uDC94"
                ).setTitleTextInfo(textInfo).setMessageTextInfo(textInfo).setCancelable(false)
                    .setOkButton(
                    ) { baseDialog, v ->
                        baseDialog.dismiss()
                        SDAnalytics().eventViewStars()
                        ToastX.showSuccess("Thanks! ❤")
                        val packageName = "com.lamesa.socialdown"
                        val appStoreLink =
                            "https://play.google.com/store/apps/details?id=$packageName"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appStoreLink))
                        context.startActivity(intent)
                        false
                    }
            }
        }
    }

}