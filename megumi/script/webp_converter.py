# -*- coding: utf-8 -*-
"""
@Time    : 2021/6/22 10:54
@Author  : 物述有栖
@File    : webp_converter.py
@DES     : webp转换器
"""
from PIL import Image
import sys
import os


def file_convert(filepath):
    # for file in os.listdir(path):
    # fileName = path + "/" + file
    im = Image.open(filepath).convert('RGB')
    save_name = filepath.replace('webp', 'png')
    im.save('{}'.format(save_name), 'png')
    os.remove(filepath)


if __name__ == '__main__':
    path = sys.argv[1]
    file_convert(path)
