package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.docs.DocCategory
import com.example.docs.DocMetadata
import com.example.docs.DocumentRepository
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Indigo100
import com.example.ui.theme.Indigo50
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Indigo700
import com.example.ui.theme.Indigo900
import com.example.ui.theme.Indigo950
import com.example.ui.theme.Rose50
import com.example.ui.theme.Rose600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900

@Composable
fun DocumentViewerScreen() {
  var selectedDoc by remember { mutableStateOf<DocMetadata?>(null) }
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf<DocCategory?>(null) }

  if (selectedDoc != null) {
    DocumentDetailView(
      doc = selectedDoc!!,
      onBack = { selectedDoc = null }
    )
  } else {
    DocumentListView(
      searchQuery = searchQuery,
      onSearchQueryChange = { searchQuery = it },
      selectedCategory = selectedCategory,
      onCategorySelect = { selectedCategory = if (selectedCategory == it) null else it },
      onDocClick = { selectedDoc = it }
    )
  }
}

@Composable
fun DocumentListView(
  searchQuery: String,
  onSearchQueryChange: (String) -> Unit,
  selectedCategory: DocCategory?,
  onCategorySelect: (DocCategory) -> Unit,
  onDocClick: (DocMetadata) -> Unit
) {
  val filteredDocs = remember(searchQuery, selectedCategory) {
    DocumentRepository.documents.filter { doc ->
      val matchesCategory = selectedCategory == null || doc.category == selectedCategory
      val matchesSearch = searchQuery.isEmpty() ||
        doc.title.contains(searchQuery, ignoreCase = true) ||
        doc.filename.contains(searchQuery, ignoreCase = true) ||
        doc.summary.contains(searchQuery, ignoreCase = true) ||
        doc.id.contains(searchQuery, ignoreCase = true)
      matchesCategory && matchesSearch
    }
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Header
    item {
      Column {
        Text(
          text = "SYSTEMS ENGINEERING REPOSITORY",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Slate400,
          letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "13 Master Specifications",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = Slate900
        )
        Text(
          text = "Complete baseline architecture & scientific verification documents",
          fontSize = 13.sp,
          color = Slate500
        )
      }
    }

    // Search Bar
    item {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("doc_search_field"),
        placeholder = { Text("Search 13 documents, requirements, protocols...", fontSize = 13.sp, color = Slate400) },
        leadingIcon = {
          Icon(Icons.Filled.Search, contentDescription = "Search", tint = Slate400)
        },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = Indigo600,
          unfocusedBorderColor = Slate200,
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White
        ),
        singleLine = true
      )
    }

    // Category Filter Chips
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        DocCategory.values().forEach { category ->
          val isSelected = selectedCategory == category
          val chipBg = if (isSelected) Indigo600 else Color.White
          val chipBorder = if (isSelected) Indigo600 else Slate200
          val chipText = if (isSelected) Color.White else Slate700

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(chipBg)
              .border(1.dp, chipBorder, RoundedCornerShape(20.dp))
              .clickable { onCategorySelect(category) }
              .padding(horizontal = 14.dp, vertical = 8.dp)
          ) {
            Text(
              text = category.label,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = chipText
            )
          }
        }
      }
    }

    // Document Items List
    items(filteredDocs) { doc ->
      DocumentCard(doc = doc, onClick = { onDocClick(doc) })
    }

    item {
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
fun DocumentCard(
  doc: DocMetadata,
  onClick: () -> Unit
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, Slate100, RoundedCornerShape(20.dp))
      .clickable { onClick() }
      .testTag("doc_card_${doc.filename}"),
    color = Color.White,
    shadowElevation = 0.5.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Indigo50)
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = doc.filename,
              fontFamily = FontFamily.Monospace,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Indigo700
            )
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Slate100)
              .padding(horizontal = 6.dp, vertical = 3.dp)
          ) {
            Text(
              text = doc.version,
              fontSize = 10.sp,
              fontWeight = FontWeight.Medium,
              color = Slate500
            )
          }
        }

        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(Emerald50)
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = doc.status.take(8).uppercase(),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = Emerald700,
            letterSpacing = 0.4.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = doc.title,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = Slate900,
        lineHeight = 20.sp
      )

      Text(
        text = doc.subtitle,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = Indigo600
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = doc.summary,
        fontSize = 12.sp,
        color = Slate500,
        lineHeight = 17.sp,
        maxLines = 2
      )
    }
  }
}

@Composable
fun DocumentDetailView(
  doc: DocMetadata,
  onBack: () -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Top Bar Action Row
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          IconButton(
            onClick = onBack,
            modifier = Modifier.size(36.dp).testTag("doc_back_button")
          ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Slate700)
          }
          Text(
            text = doc.filename,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Indigo600
          )
        }

        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(Indigo50)
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
          Text(
            text = doc.category.label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Indigo700
          )
        }
      }
    }

    // Document Header Card
    item {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(24.dp))
          .background(Indigo950),
        color = Indigo950
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Text(
            text = doc.id,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Indigo100
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = doc.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            lineHeight = 26.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = doc.subtitle,
            fontSize = 13.sp,
            color = Slate300
          )
          Spacer(modifier = Modifier.height(12.dp))
          Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            Column {
              Text("VERSION", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Slate400)
              Text(doc.version, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
            Column {
              Text("STATUS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Slate400)
              Text(doc.status, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Emerald500)
            }
          }
        }
      }
    }

    // Document Summary Callout
    item {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .border(1.dp, Indigo100, RoundedCornerShape(16.dp)),
        color = Indigo50
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Icon(
            Icons.Filled.Description,
            contentDescription = null,
            tint = Indigo600,
            modifier = Modifier.size(20.dp)
          )
          Text(
            text = doc.summary,
            fontSize = 13.sp,
            color = Indigo900,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }

    // Sections
    items(doc.sections) { section ->
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .border(1.dp, Slate100, RoundedCornerShape(20.dp)),
        color = Color.White,
        shadowElevation = 0.5.dp
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
        ) {
          Text(
            text = section.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Slate900
          )

          if (section.content.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = section.content,
              fontSize = 13.sp,
              color = Slate700,
              lineHeight = 19.sp
            )
          }

          // Render Table if available
          if (section.tableData != null && section.tableData.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
            ) {
              Column(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .border(1.dp, Slate200, RoundedCornerShape(12.dp))
              ) {
                section.tableData.forEachIndexed { rowIndex, row ->
                  val isHeader = rowIndex == 0
                  val rowBg = if (isHeader) Slate100 else Color.White
                  Row(
                    modifier = Modifier
                      .background(rowBg)
                      .border(0.5.dp, Slate200)
                      .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                  ) {
                    row.forEach { cell ->
                      Text(
                        text = cell,
                        fontSize = if (isHeader) 11.sp else 12.sp,
                        fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
                        color = if (isHeader) Slate900 else Slate700,
                        modifier = Modifier.width(130.dp)
                      )
                    }
                  }
                }
              }
            }
          }

          // Render Code Snippet if available
          if (section.codeSnippet != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
              color = Slate900
            ) {
              Text(
                text = section.codeSnippet,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = Slate100,
                modifier = Modifier.padding(14.dp),
                lineHeight = 16.sp
              )
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}
