from model import resnet101_model


def emotion_model(model_path):
    img_width, img_height = 224, 224
    num_channels = 3
    num_classes = 7
    emotion_model = resnet101_model(img_height, img_width, num_channels, num_classes)
    emotion_model.load_weights(model_path, by_name=True)
    return emotion_model