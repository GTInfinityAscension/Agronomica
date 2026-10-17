"""Approved biomass bundle item, variant A: Pillow only, RGBA 16x16, binary alpha.
Three green surface tones, a discreet outline and a two-tone fiber binding.
Run to regenerate the PNG beside this script. All coordinates are integers.
"""
from pathlib import Path
from PIL import Image, ImageDraw
PALETTE={'edge':(39,61,24,255),'shade':(66,103,32,255),
         'body':(110,151,43,255),'light':(169,191,76,255),
         'cord_dark':(104,77,41,255),'cord':(195,164,94,255)}
def generate():
    image=Image.new('RGBA',(16,16),(0,0,0,0))
    d=ImageDraw.Draw(image)
    def poly(points,c):
        d.polygon(points,fill=PALETTE[c])
    def line(points,c,width=1):
        d.line(points,fill=PALETTE[c],width=width)
    def rect(box,c):
        d.rectangle(box,fill=PALETTE[c])
    # A diagonal sheaf with visibly cut stalk ends and projecting leaf tips.
    poly([(10,1),(11,3),(13,1),(13,4),(15,3),(14,7),(11,10),(8,14),(5,15),(1,12),(3,8),(6,5)],'edge')
    poly([(10,3),(11,5),(13,3),(13,6),(10,10),(7,14),(3,12),(4,9),(7,6)],'shade')
    line([(2,11),(5,8),(8,5),(10,2)],'light')
    line([(4,13),(7,9),(10,6),(12,3)],'body',2)
    line([(6,14),(9,10),(12,7),(14,4)],'light')
    line([(8,9),(10,5),(11,3)],'shade')
    line([(5,6),(7,7),(10,9)],'cord_dark',3)
    line([(5,6),(7,7),(10,9)],'cord')
    line([(9,9),(11,11)],'cord_dark')
    line([(2,12),(4,14)],'cord')
    return image
if __name__ == '__main__':
    generate().save(Path(__file__).with_suffix('.png'))
