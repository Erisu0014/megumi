# 载入必要的模块
import os.path

from PIL import Image, ImageDraw, ImageFont
import sys


def text_to_image(text, path, name):
    arr = text.split('\\\\n')
    text.replace("\\\\n", "\n")
    width = 20 * len(max(arr, key=len, default=' ')) + 20
    height = 20 * len(arr) + 20
    im = Image.new('RGB', (width, height), (255, 255, 255))
    dr = ImageDraw.Draw(im)
    font = ImageFont.truetype('msyhl.ttc', 20)
    dr.text((10, 5), text, font=font, fill='#000000')
    im.show()
    # im.save(path + os.sep + name)
    # # pygame初始化
    # pygame.init()
    # # 待转换文字
    # # 设置字体和字号
    # font = pygame.font.SysFont('SimHei', 64)
    # # 渲染图片，设置背景颜色和字体样式,前面的颜色是字体颜色
    # ftext = font.render(text, True, (0, 0, 0), (255, 255, 255))
    # # 保存图片
    # pygame.image.save(ftext, path + os.sep + name)  # 图片保存地址


if __name__ == '__main__':
    text = sys.argv[1]
    path = sys.argv[2]
    name = sys.argv[3]
    text_to_image(text, path, name)
