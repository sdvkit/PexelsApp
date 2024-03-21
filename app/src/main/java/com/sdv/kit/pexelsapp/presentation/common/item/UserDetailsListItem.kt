package com.sdv.kit.pexelsapp.presentation.common.item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.domain.model.UserDetails
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun UserDetailsListItem(
    modifier: Modifier = Modifier,
    userDetails: UserDetails,
    isUserInFriends: Boolean,
    onAddUserClicked: (UserDetails) -> Unit,
    onRemoveUserRequestClicked: (UserDetails) -> Unit,
    requestedUsers: List<UserDetails>,
    onReject: ((UserDetails) -> Unit)? = null,
) {
    val placeholder = painterResource(R.drawable.img_placeholder)
    val isUserRequested = userDetails in requestedUsers

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .border(
                    width = Dimens.CHAT_IMAGE_BORDER_WIDTH,
                    shape = CircleShape,
                    color = AppTheme.colors.primary
                )
                .size(Dimens.CHAT_IMAGE_SIZE),
            model = userDetails.profilePictureUrl,
            contentDescription = userDetails.username,
            contentScale = ContentScale.Crop,
            error = placeholder,
            placeholder = placeholder
        )
        Spacer(modifier = Modifier.width(Dimens.PADDING_SMALL))
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = userDetails.username ?: "",
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colors.textColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = userDetails.email,
                style = AppTheme.typography.displaySmall,
                color = AppTheme.colors.secondaryTextColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        AnimatedVisibility(
            visible = isUserRequested,
        ) {
            IconButton(onClick = {
                onRemoveUserRequestClicked(userDetails)
            }) {
                Icon(
                    modifier = Modifier.size(Dimens.USER_DETAILS_ITEM_ACTION_SIZE),
                    painter = painterResource(R.drawable.ic_done),
                    contentDescription = null,
                    tint = AppTheme.colors.primary
                )
            }
        }
        AnimatedVisibility(
            visible = !isUserRequested && !isUserInFriends,
        ) {
            IconButton(onClick = {
                onAddUserClicked(userDetails)
            }) {
                Icon(
                    modifier = Modifier.size(Dimens.USER_DETAILS_ITEM_ACTION_SIZE),
                    painter = painterResource(R.drawable.ic_add_user),
                    contentDescription = null,
                    tint = AppTheme.colors.primary
                )
            }
        }

        AnimatedVisibility(
            visible = onReject != null
        ) {
            IconButton(onClick = {
                onReject ?: return@IconButton
                onReject(userDetails)
            }) {
                Icon(
                    modifier = Modifier.size(Dimens.USER_DETAILS_ITEM_CANCEL_SIZE),
                    painter = painterResource(R.drawable.ic_cross),
                    contentDescription = null,
                    tint = AppTheme.colors.primary
                )
            }
        }
    }
}

@LightAndDarkPreview
@Composable
fun UserDetailsListItemPreview() {
    AppTheme {
        Surface {
            UserDetailsListItem(
                modifier = Modifier.fillMaxWidth(),
                userDetails = UserDetails(
                    username = "Mikola Delopata",
                    email = "sudaevnikita@mail.ru"
                ),
                isUserInFriends = false,
                onAddUserClicked = { },
                requestedUsers = emptyList(),
                onRemoveUserRequestClicked = { },
                onReject = { }
            )
        }
    }
}