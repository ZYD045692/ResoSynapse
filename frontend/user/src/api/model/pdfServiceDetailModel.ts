export interface PdfServiceDetailType {
    data: string; // 图片的二进制数据，通常为 Base64 编码的字符串
    id: number; // 文件表中的 ID
    positionIndex: number; // 文件表中的位置索引
}