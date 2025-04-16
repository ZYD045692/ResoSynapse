const baseUrl1 = "http://localhost:8080/AITool/";
const baseUrl2 = "http://localhost:8000/";
const apiUrls = {
    UserLogin_API_URL:baseUrl1 + "user/login",
    UploadFiletransform_URL_API:baseUrl2+"api/transformText/",
    TextTransform_URL_API:baseUrl2+"api/transformLanguage/",
    GetPdfService_URL_API:baseUrl2+"api/inquiryService/",
    Chat_API_URL:baseUrl2+"api/chat/",
    OcrFormula_API_URL:baseUrl2+'api/ocrFormula/',
    QuirePdfServer_API_URL:baseUrl2+'api/inquiryService/',
    CreatePdfService_API_URL:baseUrl2+'api/createService/',
    DeleteService_API_URI:baseUrl2+'api/deleteService/',
    AddPage_API_URL:baseUrl2+'api/addPage/',
    DetailedPdfService_API_URL:baseUrl2+'api/detailedService/',
    UploadFiletransform_API_URL:baseUrl2+'api/transformText/',
    DeletePage_API_URL:baseUrl2+'api/deletePage/',
    DownloadPdf_API_URL:baseUrl2+'api/downloadPdf/',
    DownloadImages_API_URL:baseUrl2+'api/downloadImages/',
    MovePage_API_URL:baseUrl2+'api/movePage/',
    MDAIGC_API_URL:baseUrl2+"api/mdchat/",
}
export {apiUrls}