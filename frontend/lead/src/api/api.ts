const baseUrl1 = "http://localhost:8080/AITool/";
const baseUrl2 = "http://localhost:8000/";

const apiUrls = {
    Get_Blocks_API_URL : baseUrl1 + 'userPage/getUserBlocks',
    Get_Page_Info_API_URL : baseUrl1 + 'userPage/getUserHomePageInfo',
    FeedBack_API_URL : baseUrl1 + 'userPage/userFeedback',
    ApplyUrl_API_URL : baseUrl1 + 'userPage/userApplyUrl',
    GetEmailCaptcha_API_URL:baseUrl2 + 'api/getEmailCaptcha/',
    CheckEmailCaptcha: baseUrl2 + 'api/checkEmailCaptcha/',
    GetApplyUrlCaptcha_API_URL:baseUrl1 + "captcha/image",
    CheckApplyUrlCaptcha_API_URL:baseUrl1 + "captcha/validate",
    UserLogin_API_URL:baseUrl1 + "user/login",
    UserRegister_API_URL:baseUrl1 + "user/register",
}
export {apiUrls}