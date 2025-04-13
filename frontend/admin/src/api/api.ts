const baseUrl1 = 'http://localhost:8080/AITool/';
// const baseUrl1 = 'http://115.190.75.15:8080/AITool/';
const apiUrls = {
    GetAdminBlocks_API_URL:baseUrl1 + 'adminPage/getAdminBlocks',
    GetUersHomePageInfo_API_URL:baseUrl1 + 'userPage/getUserHomePageInfo',
    PostUpdateBlockOrder_API_URL:baseUrl1 + 'adminPage/updateBlockOrder',
    PostUpdateModuleOrder_API_URL:baseUrl1 + 'adminPage/updateModuleOrder',
    Get_Page_Info_API_URL:baseUrl1 + 'userPage/getUserHomePageInfo',
    AdminLogin_API_URL: baseUrl1 + 'admin/login',
}
export {apiUrls};