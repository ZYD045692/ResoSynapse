import os
import uuid
def get_unique_filename(filename):
    """生成唯一的文件名"""
    base, extension = os.path.splitext(filename)
    unique_filename = f"{base}_{uuid.uuid4()}{extension}"
    return unique_filename