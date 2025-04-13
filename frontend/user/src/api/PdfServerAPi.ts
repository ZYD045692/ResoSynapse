import { pdfServiceType } from "@/api/model/pdfServiceModel";
import { apiUrls } from "./api";
import axios from "axios";

export const getPdfService = async (UserId: Number): Promise<pdfServiceType[]> => {
    try {
        const params = {
            userId: UserId
        };
        const response = await axios.get(apiUrls.GetPdfService_URL_API, { params });
        if (response.status === 200 && response.data.stateCode === 1) {
            return response.data.serviceInfo || [];
        }
        console.warn('Invalid response:', response.data);
        return [];
    } catch (error) {
        console.error('Error fetching PDF services:', error);
        return [];
    }
};



